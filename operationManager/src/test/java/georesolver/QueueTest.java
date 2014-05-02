package georesolver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-config.xml" })
public class QueueTest {

	@Autowired
	RabbitTemplate amqpTemplate;
	
	@Test
	public void testGeoResolverQueue() {
		amqpTemplate.convertAndSend("geoResolverQueue", "hello");
		Object message = amqpTemplate.receiveAndConvert("geoResolverQueue");
		assertEquals("hello", (String) message);
	}

	@Test
	public void testNewsBeeperQueue() {
		amqpTemplate.convertAndSend("newsBeeperQueue", "hello");
		Object message = amqpTemplate.receiveAndConvert("newsBeeperQueue");
		assertEquals("hello", (String) message);
	}
}
