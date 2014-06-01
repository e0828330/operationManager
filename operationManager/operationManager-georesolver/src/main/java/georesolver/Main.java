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
import service.exceptions.ServiceException;
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
		prog.listenOnQueue();
	}

	private void listenOnQueue() {
		queueService.registerListener(RabbitMQConfig.GEORESOLVER_Q, new IQueueListener() {
			@Override
			public void handleMessage(Message m) {
				OPSlotDTO slotDTO = (OPSlotDTO) m;
	
				/* We need to send a notification to both patient and doctor */
				NotificationDTO notificationDoc = new NotificationDTO();
				NotificationDTO notificationPat = new NotificationDTO();
	
				OPSlot opSlot = null;
				
				try {
					opSlot = geoResolverService.findSlot(slotDTO);
				} catch (ServiceException e) {
					notificationDoc.setMessage("Registrierung fehlgeschlagen!");
					notificationDoc.setType(NotificationType.RESERVATION_FAILED);
					notificationPat.setMessage("Registrierung fehlgeschlagen!");
					notificationPat.setType(NotificationType.RESERVATION_FAILED);
				}		

				if (opSlot != null) {
					opSlotService.saveOPSlot(opSlot);

					notificationDoc.setMessage("Registrierung erfolgreich!");
					notificationDoc.setType(NotificationType.RESERVATION_SUCESSFULL);
					notificationPat.setMessage("Registrierung erfolgreich!");
					notificationPat.setType(NotificationType.RESERVATION_SUCESSFULL);
					
				}
				else {
					notificationDoc.setMessage("Registrierung fehlgeschlagen!");
					notificationDoc.setType(NotificationType.RESERVATION_FAILED);
					notificationPat.setMessage("Registrierung fehlgeschlagen!");
					notificationPat.setType(NotificationType.RESERVATION_FAILED);
				}
				
				notificationDoc.setRecipientID(slotDTO.getDoctorID());
				notificationPat.setRecipientID(slotDTO.getPatientID());
				
				queueService.sendToNewsBeeper(notificationDoc);
				queueService.sendToNewsBeeper(notificationPat);
			}
		});
	}
}
