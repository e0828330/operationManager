package service;
import static org.junit.Assert.*;
import model.Doctor;
import model.NotificationType;
import model.Patient;
import model.Role;
import model.dto.NotificationDTO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import repository.DoctorRepository;
import repository.PatientRepository;
import service.INewsbeeperService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-config.xml" })

public class NewsbeeperTest {
	

	@Autowired
	private INewsbeeperService newsbeeperService;
	
	@Autowired
	private DoctorRepository repoD;
	
	@Autowired
	private PatientRepository repoP;

	@Test
	public void testhandleNotificationNull() {
		assertNull(newsbeeperService.handleNotification(null));
	}
	
	@Test
	public void testhandleNotification01() {
		NotificationDTO dto = new NotificationDTO();

		assertNull(newsbeeperService.handleNotification(dto));
	}	
	
	@Test
	public void testhandleNotification02() {
		NotificationDTO dto = new NotificationDTO();
		dto.setMessage("Test");
		dto.setType(NotificationType.RESERVATION_SUCESSFULL);
		dto.setRecipientID("test_id");
		dto.setOpSlotID("opslot_id");

		assertNull(newsbeeperService.handleNotification(dto));
	}
	
	@Test
	public void testhandleNotification03() {
		NotificationDTO dto = new NotificationDTO();
		dto.setMessage("Test");
		dto.setType(NotificationType.RESERVATION_SUCESSFULL);
		
		dto.setOpSlotID("opslot_id");
		
		Doctor d = new Doctor();
		d.setFirstName("fname");
		d.setLastName("lname");
		d.setRole(Role.DOCTOR);
		d.setUsername("uname01");
				
		repoD.save(d);
		
		dto.setRecipientID(d.getId());

		assertEquals(d.getUsername(), newsbeeperService.handleNotification(dto).getRecipient().getUsername());
		
		repoD.delete(d);
	}
	
	@Test
	public void testhandleNotification04() {
		NotificationDTO dto = new NotificationDTO();
		dto.setMessage("Test");
		dto.setType(NotificationType.RESERVATION_SUCESSFULL);
		
		dto.setOpSlotID("opslot_id");
		
		Patient p = new Patient();
		p.setFirstName("fname");
		p.setLastName("lname");
		p.setRole(Role.PATIENT);
		p.setUsername("uname02");
				
		repoP.save(p);
		
		dto.setRecipientID(p.getId());

		assertEquals(p.getUsername(), newsbeeperService.handleNotification(dto).getRecipient().getUsername());
		
		repoP.delete(p);
	}	

}
