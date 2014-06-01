package newsbeeper;

import model.Notification;
import model.Patient;
import model.User;
import model.dto.Message;
import model.dto.NotificationDTO;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import service.INotificationService;
import service.IOPSlotService;
import service.IPatientService;
import service.IQueueListener;
import service.IQueueService;
import config.RabbitMQConfig;

@Component
public class Main implements InitializingBean {

	@Autowired
	private IQueueService queueService;

	@Autowired
	private IPatientService patientService;
	
	@Autowired
	private IOPSlotService opSlotService;
	
	@Autowired
	private INotificationService notificationService;
	
	
	private static ApplicationContext context;
		
	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("/spring/application-config.xml");
		Main prog = new Main();
		AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
		factory.autowireBeanProperties(prog, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
		factory.initializeBean(prog, "newsbeeper");
		prog.listenOnQueue();
	}	
	
	private void listenOnQueue() {
		queueService.registerListener(RabbitMQConfig.NEWSBEEPER_Q, new IQueueListener() {
			@Override
			public void handleMessage(Message m) {
				NotificationDTO notificationDTO = (NotificationDTO) m;
				Notification notification = new Notification();
				notification.setMessage(notificationDTO.getMessage());
				
				Patient patient = patientService.getById(notificationDTO.getRecipientID());
				// TODO: Check if patient is null etc.
				notification.setRecipient((User)patient);
				// TODO: Implement opSlotService.getById() use and set 
				//notification.setSlot(slot);
				
				notificationService.save(notification);
			}
		});			
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
}
