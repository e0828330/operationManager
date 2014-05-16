package georesolver;


import model.OPSlot;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import service.IQueueListener;
import service.IQueueService;
import service.real.GeoResolverListener;
import service.real.QueueService;

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
		IQueueListener listener = new GeoResolverListener();
		service.registerListener(QueueService.GEORESOLVER_Q, listener);
		service.sendToGeoResolver(slot);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
