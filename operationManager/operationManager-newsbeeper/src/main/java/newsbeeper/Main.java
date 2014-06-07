package newsbeeper;

import java.util.Date;

import model.Doctor;
import model.Hospital;
import model.Notification;
import model.OPSlot;
import model.Patient;
import model.User;
import model.dto.Message;
import model.dto.NotificationDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import service.INewsbeeperService;
import service.INotificationService;
import service.IOPSlotService;
import service.IPatientService;
import service.IQueueListener;
import service.IQueueService;
import config.RabbitMQConfig;

@Component
public class Main implements InitializingBean {

	private Logger logger = LoggerFactory.getLogger(Main.class);

	@Autowired
	private IQueueService queueService;

	@Autowired
	private INewsbeeperService newsbeeperService;

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
	}

	private void listenOnQueue() {
		queueService.registerListener(RabbitMQConfig.NEWSBEEPER_Q, new IQueueListener() {
			@Override
			public void handleMessage(Message m) {
				try {
					NotificationDTO notificationDTO = (NotificationDTO) m;
					Notification notification = new Notification();
					notification.setMessage(notificationDTO.getMessage());
					notification.setType(notificationDTO.getType());

					logger.info("Got request: " + notificationDTO);

					Patient patient = patientService.getById(notificationDTO.getRecipientID());
					OPSlot slot = notificationDTO.getOpSlotID() == null ? null : opSlotService.getById(notificationDTO.getOpSlotID());

					if (patient != null) {
						notification.setRecipient((User) patient);
					} else {
						Doctor doctor = newsbeeperService.getDoctorById(notificationDTO.getRecipientID());
						if (doctor != null) {
							notification.setRecipient((User) doctor);
						} else {
							Hospital hospital = newsbeeperService.getHospitalById(notificationDTO.getRecipientID());
							if (hospital != null) {
								notification.setRecipient((User) hospital);
							} else {
								logger.info("No User found with id = " + notificationDTO.getRecipientID());
								return;
							}
						}
					}

					if (slot == null) {
						logger.info("No slot found.");
					}

					notification.setSlot(slot);
					notification.setTimestamp(new Date());

					notificationService.save(notification);
				} catch (Exception e) {
					logger.error("Failed to handle message", e);

				}
			}
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Started listening");
		listenOnQueue();
	}
}
