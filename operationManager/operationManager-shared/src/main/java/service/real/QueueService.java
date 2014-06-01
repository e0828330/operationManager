package service.real;

import java.util.HashMap;

import model.dto.NotificationDTO;
import model.dto.OPSlotDTO;

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
	public void sendToGeoResolver(OPSlotDTO slot) {
		rabbitTemplate.convertAndSend(RabbitMQConfig.GEORESOLVER_Q, slot);
	}

	@Override
	public void sendToNewsBeeper(NotificationDTO notification) {
		rabbitTemplate.convertAndSend(RabbitMQConfig.NEWSBEEPER_Q, notification);

	}

	@Override
	public void registerListener(String queueName, IQueueListener listener) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(
				connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(new MessageListenerAdapter(listener));
		container.start();
		this.listener.put(listener, container);
	}

	@Override
	public void unregisterListener(String queueName, IQueueListener listener) {
		if (this.listener.containsKey(listener)) {
			this.listener.get(listener).stop();
			this.listener.remove(listener);
		}
	}
	
	

}
