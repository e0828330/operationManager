package georesolver;


import model.OPSlot;
import model.dto.Message;
import model.dto.OPSlotDTO;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import service.IQueueListener;
import service.IQueueService;
import config.RabbitMQConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-config.xml" })
public class QueueServiceTest {

	private static OPSlot slot;
	
	@Autowired
	private IQueueService service;
	
	@BeforeClass
	public static void initialize() {
		slot = new OPSlot();
		slot.setId("1234");
	}
	
	// TODO: Just for testing service in development
	@Test
	public void testService() {
		final int worked = 0;
		service.registerListener(RabbitMQConfig.GEORESOLVER_Q, new IQueueListener() {
			
			@Override
			public void handleMessage(Message m) {
				OPSlotDTO slot = (OPSlotDTO) m;
				
			}
		});
		service.sendToGeoResolver(slot);
		
	}

}
