package testData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.NoSuchAlgorithmException;

import model.Patient;
import model.Role;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import repository.DoctorRepository;
import repository.HospitalRepository;
import repository.PatientRepository;
import utils.Utils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-config.xml" })
public class TestData {
	@Autowired 
	private PatientRepository patientRepo;
	
	@Autowired 
	private HospitalRepository hsRepo;
	
	@Autowired 
	private DoctorRepository doctorRepo;
	
	@Autowired 
	private MongoTemplate mongoTemplate;
	
	@Test
	public void testPatientLogin() throws NoSuchAlgorithmException {
		Patient p = patientRepo.findByUsernameAndPassword("abesser", Utils.computeHash("test01"));
		assertTrue(p != null);
		assertEquals(p.getLastName(), "Abesser");
	}
	
	@Before
	public void generateTestDB() throws NoSuchAlgorithmException {
		
		mongoTemplate.remove(new Query(), "patient");
		
		// Patients
		Patient p = new Patient();
		p.setFirstName("Adelheid");
		p.setLastName("Abesser");
		p.setUsername("abesser");
		p.setPassword(Utils.computeHash("test01"));
		p.setRole(Role.PATIENT);
		patientRepo.save(p);

		p = new Patient();
		p.setFirstName("Peter");
		p.setLastName("Berger");
		p.setUsername("berger");
		p.setPassword(Utils.computeHash("test02"));
		p.setRole(Role.PATIENT);
		patientRepo.save(p);
		
		p = new Patient();
		p.setFirstName("Beatrix");
		p.setLastName("Bauer");
		p.setUsername("bauer");
		p.setPassword(Utils.computeHash("test03"));
		p.setRole(Role.PATIENT);
		patientRepo.save(p);

		p = new Patient();
		p.setFirstName("Franz");
		p.setLastName("Fiedler");
		p.setUsername("franz");
		p.setPassword(Utils.computeHash("test04"));
		patientRepo.save(p);
		
		p = new Patient();
		p.setFirstName("Gloria");
		p.setLastName("Geraus");
		p.setUsername("geraus");
		p.setPassword(Utils.computeHash("test05"));
		p.setRole(Role.PATIENT);
		patientRepo.save(p);
		
		p = new Patient();
		p.setFirstName("Manfred");
		p.setLastName("Takacs");
		p.setUsername("takacs");
		p.setPassword(Utils.computeHash("test06"));
		p.setRole(Role.PATIENT);
		patientRepo.save(p);
	}
	
	@After
	public void cleanDataBase() {
		mongoTemplate.remove(new Query(), "patient");
	}
}
