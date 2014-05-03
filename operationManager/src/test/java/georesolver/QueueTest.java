package georesolver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-config.xml" })
public class QueueTest {

	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	ConnectionFactory connectionFactory;
	
	@Test
	public void testGeoResolverQueue() {
		rabbitTemplate.convertAndSend("geoResolverQueue", "hello");
		Object message = rabbitTemplate.receiveAndConvert("geoResolverQueue");
		assertEquals("hello", (String) message);
	}

	@Test
	public void testNewsBeeperQueue() {
		rabbitTemplate.convertAndSend("newsBeeperQueue", "hello");
		Object message = rabbitTemplate.receiveAndConvert("newsBeeperQueue");
		assertEquals("hello", (String) message);
	}
	
	@Test
	public void asyncGeoQueueTest() {
		final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames("geoResolverQueue");
		container.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message msg) {
				assertEquals("hello", new String(msg.getBody()));
				container.stop();
			}
		});
		container.start();
		rabbitTemplate.convertAndSend("geoResolverQueue", "hello");
	}
	
	
	@Test
	public void asyncNewsQueueTest() {
		final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames("newsBeeperQueue");
		container.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message msg) {
				assertEquals("hello", new String(msg.getBody()));
				container.stop();
			}
		});
		container.start();
		rabbitTemplate.convertAndSend("newsBeeperQueue", "hello");
	}
}
