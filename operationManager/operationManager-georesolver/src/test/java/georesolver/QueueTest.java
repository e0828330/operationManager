package georesolver;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import service.real.QueueService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-config.xml" })
public class QueueTest {

	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	ConnectionFactory connectionFactory;
	
	@Test
	public void testGeoResolverQueue() {
		rabbitTemplate.convertAndSend("geoResolverQueue", "hello-testGeoResolverQueue");
		Object message = rabbitTemplate.receiveAndConvert("geoResolverQueue");
		assertEquals("hello-testGeoResolverQueue", (String) message);
	}

	@Test
	public void testNewsBeeperQueue() {
		rabbitTemplate.convertAndSend("newsBeeperQueue", "hello-testNewsBeeperQueue");
		Object message = rabbitTemplate.receiveAndConvert("newsBeeperQueue");
		assertEquals("hello-testNewsBeeperQueue", (String) message);
	}

}
