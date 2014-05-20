package georesolver;

import model.NotificationType;
import model.OPSlot;
import model.dto.Message;
import model.dto.NotificationDTO;
import model.dto.OPSlotDTO;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import service.IGeoResolverService;
import service.IOPSlotService;
import service.IQueueListener;
import service.IQueueService;
import config.RabbitMQConfig;

@Component
public class Main implements InitializingBean {


	@Autowired
	private IQueueService queueService;

	@Autowired
	private IOPSlotService opSlotService;

	@Autowired
	private IGeoResolverService geoResolverService;

	private static ApplicationContext context;

	@Override
	public void afterPropertiesSet() throws Exception {		
	}
	
	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("/spring/application-config.xml");
		Main prog = new Main();
		AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
		factory.autowireBeanProperties(prog, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
		factory.initializeBean(prog, "georesolver");
		prog.run();
	}

	private void registerListener() {
		queueService.registerListener(RabbitMQConfig.GEORESOLVER_Q, new IQueueListener() {
			@Override
			public void handleMessage(Message m) {
				OPSlotDTO slotDTO = (OPSlotDTO) m;
				OPSlot opSlot = geoResolverService.findSlot(slotDTO);

				NotificationDTO notification = new NotificationDTO();

				if (opSlot != null) {
					opSlotService.saveOPSlot(opSlot);
					notification.setMessage("Registrierung erfolgreich!"); // TODO: Better message
					notification.setType(NotificationType.RESERVATION_SUCESSFULL);

				}
				else {
					notification.setMessage("Registrierung fehlgeschlagen!"); // TODO: Better message
					notification.setType(NotificationType.RESERVATION_FAILED);
				}

				queueService.sendToNewsBeeper(notification);
			}
		});
	}

	void run() {
		registerListener();
	}
}
