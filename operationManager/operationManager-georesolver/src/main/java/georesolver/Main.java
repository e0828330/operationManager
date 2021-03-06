package georesolver;

import model.NotificationType;
import model.OPSlot;
import model.OperationStatus;
import model.dto.Message;
import model.dto.NotificationDTO;
import model.dto.OPSlotDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private Logger logger = LoggerFactory.getLogger(Main.class);

	@Autowired
	private IQueueService queueService;

	@Autowired
	private IOPSlotService opSlotService;

	@Autowired
	private IGeoResolverService geoResolverService;

	private static ApplicationContext context;

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Started listening");
		listenOnQueue();
	}

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("/spring/application-config.xml");
		Main prog = new Main();
		AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
		factory.autowireBeanProperties(prog, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
	}

	private void listenOnQueue() {
		queueService.registerListener(RabbitMQConfig.GEORESOLVER_Q, new IQueueListener() {
			@Override
			public void handleMessage(Message m) {
				try {
					OPSlotDTO slotDTO = (OPSlotDTO) m;

					logger.info("Got request: " + m);
					/*
					 * We need to send a notification to patient, doctor and
					 * hospital
					 */
					NotificationDTO notificationDoc = new NotificationDTO();
					NotificationDTO notificationPat = new NotificationDTO();
					NotificationDTO notificationHos = new NotificationDTO();

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
						opSlot.setStatus(OperationStatus.reserved);
						opSlotService.saveOPSlot(opSlot);

						notificationDoc.setOpSlotID(opSlot.getId());
						notificationDoc.setMessage("Registrierung erfolgreich!");
						notificationDoc.setType(NotificationType.RESERVATION_SUCESSFULL);

						notificationPat.setOpSlotID(opSlot.getId());
						notificationPat.setMessage("Registrierung erfolgreich!");
						notificationPat.setType(NotificationType.RESERVATION_SUCESSFULL);

						notificationHos.setOpSlotID(opSlot.getId());
						notificationHos.setMessage("Registrierung erfolgreich!");
						notificationHos.setType(NotificationType.RESERVATION_SUCESSFULL);
						notificationHos.setRecipientID(opSlot.getHospital().getId());

						queueService.sendToNewsBeeper(notificationHos);
					} else {
						notificationDoc.setMessage("Registrierung fehlgeschlagen!");
						notificationDoc.setType(NotificationType.RESERVATION_FAILED);
						notificationPat.setMessage("Registrierung fehlgeschlagen!");
						notificationPat.setType(NotificationType.RESERVATION_FAILED);
					}

					notificationDoc.setRecipientID(slotDTO.getDoctorID());
					notificationPat.setRecipientID(slotDTO.getPatientID());

					queueService.sendToNewsBeeper(notificationDoc);
					queueService.sendToNewsBeeper(notificationPat);
				} catch (Exception e) {
					logger.error("Failed to handle message", e);
				}
			}
		});
	}
}
