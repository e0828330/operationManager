package service.jms;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import model.OPSlot;
import model.dto.OPSlotDTO;
import service.IQueueListener;
import service.IQueueService;

public class JMSService implements IQueueService {
	
	

	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	ConnectionFactory connectionFactory;
	
	private HashMap<IQueueListener, SimpleMessageListenerContainer> listener = new HashMap<>();
	
	@Override
	public void sendToGeoResolver(OPSlot slot) {
		rabbitTemplate.convertAndSend("geoResolverQueue", slot);
	}

	@Override
	public void sendToNewsBeeper(OPSlot slot) {
		rabbitTemplate.convertAndSend("newsBeeperQueue", slot);
		
	}

	@Override
	public void registerListener(String queueName, final IQueueListener listener) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				try {
					// GeoResolver
					if (listener instanceof JMSGeoResolverListener) {
						if (message instanceof ObjectMessage) {
							ObjectMessage objMsg = (ObjectMessage) message;
							if (objMsg.getObject() instanceof OPSlot) {
								OPSlot opslot = (OPSlot) objMsg.getObject();
								OPSlotDTO dto = new OPSlotDTO();
								dto.setId(opslot.getId());
								dto.setDate(opslot.getDate());
								dto.setPatient(opslot.getPatient());
								dto.setType(opslot.getType());
								listener.onMessage(dto);
							}	
						}
					}
					
					// NewsBeeper
					if (listener instanceof JMSNewsBeeperListener) {
						if (message instanceof ObjectMessage) {
							ObjectMessage objMsg = (ObjectMessage) message;
							if (objMsg.getObject() instanceof OPSlot) {
								OPSlot opslot = (OPSlot) objMsg.getObject();
								OPSlotDTO dto = new OPSlotDTO();
								dto.setId(opslot.getId());
								dto.setDate(opslot.getDate());
								dto.setPatient(opslot.getPatient());
								dto.setType(opslot.getType());
								listener.onMessage(dto);
							}	
						}
					}					
				} catch (JMSException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
		
		this.listener.put(listener, container);
	}

}
