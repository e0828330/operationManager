package queue;


import static org.junit.Assert.*;

import java.util.Date;
import java.util.concurrent.Semaphore;

import model.NotificationType;
import model.OPSlot;
import model.Patient;
import model.dto.Message;
import model.dto.NotificationDTO;
import model.dto.OPSlotDTO;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import service.IQueueListener;
import service.IQueueService;
import config.RabbitMQConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/test-config.xml" })
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
	public void testGeoQueue() {
		OPSlotDTO dto = new OPSlotDTO();
		s = new Semaphore(0);
		service.sendToGeoResolver(dto);

		service.registerListener(RabbitMQConfig.GEORESOLVER_Q, new IQueueListener() {
			@Override
			public void handleMessage(Message m) {
				value = true;
				s.release();
			}
		});
		
		try {
			s.acquire();
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
		assertTrue(value);
	}
	
	
	@Test (timeout = 2000)
	public void testNewsQueue() {
		NotificationDTO notification = new NotificationDTO();
		notification.setMessage("TEST");
		notification.setOpSlotID(null);
		notification.setRecipientID("xxx");
		notification.setType(NotificationType.RESERVATION_FAILED);
		s = new Semaphore(0);
		service.sendToNewsBeeper(notification);

		service.registerListener(RabbitMQConfig.NEWSBEEPER_Q, new IQueueListener() {
			@Override
			public void handleMessage(Message m) {
				value = ((NotificationDTO) m).getMessage().equals("TEST");
				s.release();
			}
		});
		
		try {
			s.acquire();
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
		assertTrue(value);
	}

}
