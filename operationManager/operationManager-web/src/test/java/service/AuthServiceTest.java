package service;

import static org.junit.Assert.assertEquals;
import model.Role;
import model.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import testData.DatabaseTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-config.xml" })
public class AuthServiceTest extends DatabaseTest {
	
	@Autowired
	@Qualifier("getAuthenticationService")
	private IAuthenticationService service;

	@Test
	public void testLogin_correct_patient_credentials() {
		User user = service.authenticate("abesser", "test01");
		assertEquals(Role.PATIENT, user.getRole());
	}
	
	@Test
	public void testLogin_correct_doctor_credentials() {
		User user = service.authenticate("maria", "test09");
		assertEquals(Role.DOCTOR, user.getRole());
	}

	@Test
	public void testLogin_correct_hospital_credentials() {
		User user = service.authenticate("lkhbaden", "test12");
		assertEquals(Role.HOSPITAL, user.getRole());
	}

	@Test
	public void testLogin_wrong_credentials() {
		User user = service.authenticate("wrong", "wrong");
		assertEquals(Role.DEFAULT, user.getRole());
	}
}
