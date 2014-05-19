package georesolver;


import static org.junit.Assert.*;

import java.util.Date;
import java.util.concurrent.Semaphore;

import model.OPSlot;
import model.Patient;
import model.dto.Message;
import model.dto.OPSlotDTO;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import service.IQueueListener;
import service.IQueueService;
import config.RabbitMQConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-config.xml" })
public class QueueServiceTest {

	private static OPSlot slot;
	
	private boolean value = false;
	
	private Semaphore s;
	
	@Autowired
	private IQueueService service;
	
	@BeforeClass
	public static void initialize() {
		slot = new OPSlot();
		slot.setId("1234");
		slot.setDate(new Date());
		slot.setTo(new Date());
		slot.setFrom(new Date());
		Patient p = new Patient();
		p.setPosition(new Point(0., 0.));
		slot.setPatient(p);
	}
	
	@Before
	public void setup() {
		value = false;
	}
	
	@Test (timeout = 2000)
	public void testService() {
		
		s = new Semaphore(0);

		service.registerListener(RabbitMQConfig.GEORESOLVER_Q, new IQueueListener() {
			@Override
			public void handleMessage(Message m) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					fail(e.getMessage());
				}
				value = true;
				s.release();
			}
		});
		
		OPSlotDTO dto = new OPSlotDTO();

		service.sendToGeoResolver(dto);
		try {
			s.acquire();
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
		assertTrue(value);
	}

}
