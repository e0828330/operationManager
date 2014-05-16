package service.real;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import config.RabbitMQConfig;
import model.Notification;
import model.OPSlot;
import model.dto.NotificationDTO;
import model.dto.OPSlotDTO;
import service.IQueueListener;
import service.IQueueService;

@Service
public class QueueService implements IQueueService {

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	ConnectionFactory connectionFactory;

	private HashMap<IQueueListener, SimpleMessageListenerContainer> listener = new HashMap<>();

	public static final String GEORESOLVER_Q = "geoResolverQueue";
	public static final String NEWSBEEPER_Q = "newsBeeperQueue";

	@Override
	public void sendToGeoResolver(OPSlot slot) {
		rabbitTemplate.convertAndSend(GEORESOLVER_Q, slot);
	}

	@Override
	public void sendToNewsBeeper(Notification notification) {
		rabbitTemplate.convertAndSend(NEWSBEEPER_Q, notification);

	}

	@Override
	public void registerListener(String queueName, final IQueueListener listener) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(
				connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(new MessageListenerAdapter(listener));
		container.start();
/*
			@Override
			public void onMessage(Message message) {
				try {
					// GeoResolver
					
					if (message.getBody() instanceof OPSlot) {

						OPSlot opslot = (OPSlot) objMsg.getObject();
						OPSlotDTO dto = new OPSlotDTO();
						dto.setId(opslot.getId());
						dto.setDate(opslot.getDate());
						dto.setPatient(opslot.getPatient());
						dto.setType(opslot.getType());
						listener.onMessage(dto);
					}
					if (message instanceof Notification) {
						Notification notification = (Notification) objMsg
								.getObject();
						NotificationDTO dto = new NotificationDTO();
						dto.setNotification(notification);
						listener.onMessage(dto);

					}

				} catch (JMSException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});*/

		// this.listener.put(listener, container);
	}

}
