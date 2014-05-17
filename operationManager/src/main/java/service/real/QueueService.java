package service.real;

import java.util.HashMap;

import model.Notification;
import model.OPSlot;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.IQueueListener;
import service.IQueueService;
import config.RabbitMQConfig;

@Service
public class QueueService implements IQueueService {

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	ConnectionFactory connectionFactory;

	private HashMap<IQueueListener, SimpleMessageListenerContainer> listener = new HashMap<>();


	@Override
	public void sendToGeoResolver(OPSlot slot) {
		rabbitTemplate.convertAndSend(RabbitMQConfig.GEORESOLVER_Q, slot);
	}

	@Override
	public void sendToNewsBeeper(Notification notification) {
		rabbitTemplate.convertAndSend(RabbitMQConfig.NEWSBEEPER_Q, notification);

	}

	@Override
	public void registerListener(String queueName, IQueueListener listener) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(
				connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(new MessageListenerAdapter(listener));
		container.start();
	}

}
