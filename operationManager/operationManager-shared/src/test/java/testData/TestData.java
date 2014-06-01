package testData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import model.Doctor;
import model.Hospital;
import model.OPSlot;
import model.OperationStatus;
import model.OperationType;
import model.Patient;
import model.Role;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import repository.DoctorRepository;
import repository.HospitalRepository;
import repository.OPSlotRepository;
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
	private OPSlotRepository opSlotRepo;
	
	@Autowired 
	private DoctorRepository doctorRepo;


	@Test
	public void testPatientLogin() throws NoSuchAlgorithmException {
		Patient p = patientRepo.findByUsernameAndPassword("abesser", Utils.computeHash("test01"));
		assertNotNull(p);
		assertEquals(p.getLastName(), "Abesser");
	}
	
	/**
	 * Adds some empty slots for a given hospital
	 * 
	 * @param hospital
	 */
	private void fillinSlots(Hospital hospital) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(new Date());
		calDate.set(Calendar.HOUR_OF_DAY, 0);
		calDate.set(Calendar.MINUTE, 0);
		calDate.add(Calendar.DATE, 45);

		
		for (int i = 0; i < 7; i ++) {
			Calendar calFrom = Calendar.getInstance();
			calFrom.setTime(calDate.getTime());
				
			Calendar calTo = Calendar.getInstance();
			calTo.setTime(calDate.getTime());
			
			for (int j = 8; j <= 20; j+=2) {
				calFrom.add(Calendar.HOUR_OF_DAY, j);
				calTo.add(Calendar.HOUR_OF_DAY, j + 2);
				OPSlot slot = new OPSlot();
				slot.setDate(calDate.getTime());
				slot.setFrom(calFrom.getTime());
				slot.setTo(calTo.getTime());
				slot.setHospital(hospital);
				slot.setStatus(OperationStatus.free);
				slot.setType(OperationType.values()[i * j % OperationType.values().length]);
				opSlotRepo.save(slot);
			}
			
			calDate.add(Calendar.DATE, 1);
		}
		
	}
	
	@Before
	public void generateTestDB() throws NoSuchAlgorithmException {
		cleanDataBase();
		
		// Patients
		Patient p = new Patient();
		p.setFirstName("Adelheid");
		p.setLastName("Abesser");
		p.setUsername("abesser");
		p.setPassword(Utils.computeHash("test01"));
		p.setRole(Role.PATIENT);
		p.setPosition(new Point(48.2065, 16.384821));
		patientRepo.save(p);

		p = new Patient();
		p.setFirstName("Peter");
		p.setLastName("Berger");
		p.setUsername("berger");
		p.setPassword(Utils.computeHash("test02"));
		p.setRole(Role.PATIENT);
		p.setPosition(new Point(48.2065, 16.384821));
		patientRepo.save(p);
		
		p = new Patient();
		p.setFirstName("Beatrix");
		p.setLastName("Bauer");
		p.setUsername("bauer");
		p.setPassword(Utils.computeHash("test03"));
		p.setRole(Role.PATIENT);
		p.setPosition(new Point(48.2065, 16.384821));
		patientRepo.save(p);

		p = new Patient();
		p.setFirstName("Franz");
		p.setLastName("Fiedler");
		p.setUsername("franz");
		p.setPassword(Utils.computeHash("test04"));
		p.setRole(Role.PATIENT);
		p.setPosition(new Point(48.2065, 16.384821));
		patientRepo.save(p);
		
		p = new Patient();
		p.setFirstName("Gloria");
		p.setLastName("Geraus");
		p.setUsername("geraus");
		p.setPassword(Utils.computeHash("test05"));
		p.setRole(Role.PATIENT);
		p.setPosition(new Point(48.2065, 16.384821));
		patientRepo.save(p);
		
		p = new Patient();
		p.setFirstName("Manfred");
		p.setLastName("Takacs");
		p.setUsername("takacs");
		p.setPassword(Utils.computeHash("test06"));
		p.setRole(Role.PATIENT);
		p.setPosition(new Point(48.2065, 16.384821));
		patientRepo.save(p);

		// Doctors
		Doctor d = new Doctor();
		d.setFirstName("Albert");
		d.setLastName("Aufschneider");
		d.setUsername("albert");
		d.setPassword(Utils.computeHash("test07"));
		d.setRole(Role.DOCTOR);
		doctorRepo.save(d);
		
		d = new Doctor();
		d.setFirstName("Emily");
		d.setLastName("Ehmoser");
		d.setUsername("emily");
		d.setPassword(Utils.computeHash("test08"));
		d.setRole(Role.DOCTOR);
		doctorRepo.save(d);
		
		d = new Doctor();
		d.setFirstName("Adam");
		d.setLastName("Augefehler");
		d.setUsername("adam");
		d.setPassword(Utils.computeHash("test08"));
		d.setRole(Role.DOCTOR);
		doctorRepo.save(d);
		
		d = new Doctor();
		d.setFirstName("Maria");
		d.setLastName("Morks");
		d.setUsername("maria");
		d.setPassword(Utils.computeHash("test09"));
		d.setRole(Role.DOCTOR);
		doctorRepo.save(d);
		
		// Hospitals
		Hospital h = new Hospital();
		h.setName("SMZ Ost");
		h.setUsername("smzost");
		h.setPassword(Utils.computeHash("test10"));
		h.setRole(Role.HOSPITAL);
		h.setPosition(new Point(48.219218, 16.464200));
		hsRepo.save(h);
		fillinSlots(h);
		
		
		h = new Hospital();
		h.setName("LKH Krems");
		h.setUsername("lkhkrems");
		h.setPassword(Utils.computeHash("test11"));
		h.setRole(Role.HOSPITAL);
		h.setPosition(new Point(48.412252, 15.614987));
		hsRepo.save(h);
		fillinSlots(h);
	
		h = new Hospital();
		h.setName("LKH Baden");
		h.setUsername("lkhbaden");
		h.setPassword(Utils.computeHash("test12"));
		h.setRole(Role.HOSPITAL);
		h.setPosition(new Point(48.000463, 16.254158));
		hsRepo.save(h);
		fillinSlots(h);
		
		h = new Hospital();
		h.setName("Rudolfinerhaus");
		h.setUsername("rudolfinerhaus");
		h.setPassword(Utils.computeHash("test13"));
		h.setRole(Role.HOSPITAL);
		h.setPosition(new Point(48.243323, 16.347580));
		hsRepo.save(h);
		fillinSlots(h);
	}

	public void cleanDataBase() {
		patientRepo.deleteAll();
		doctorRepo.deleteAll();
		hsRepo.deleteAll();
		opSlotRepo.deleteAll();
	}
}
