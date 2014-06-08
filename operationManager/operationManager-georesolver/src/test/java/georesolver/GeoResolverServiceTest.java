package georesolver;

import java.util.Date;

import model.OPSlot;
import model.OperationType;
import model.dto.OPSlotDTO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import repository.DoctorRepository;
import repository.PatientRepository;
import service.IGeoResolverService;
import service.exceptions.ServiceException;
import testData.DatabaseTest;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-config.xml" })
public class GeoResolverServiceTest extends DatabaseTest {
	@Autowired
	private IGeoResolverService service;

	@Autowired
	private PatientRepository patientRepo;
	
	@Autowired
	private DoctorRepository doctorRepo;
	
	@Test(expected=ServiceException.class)
	/**
	 * Should throw an exception because the doctor is invalid
	 * 
	 * @throws ServiceException
	 */
	public void test_InvalidDoctor() throws ServiceException {
		OPSlotDTO slotDTO = new OPSlotDTO();
		slotDTO.setPatientID(patientRepo.findAll().get(0).getId());
		slotDTO.setDoctorID("invalid");
		service.findSlot(slotDTO);
	}
	
	@Test(expected=ServiceException.class)
	/**
	 * Should throw an exception because the patient is invalid
	 * 
	 * @throws ServiceException
	 */
	public void test_InvalidPatient() throws ServiceException {
		OPSlotDTO slotDTO = new OPSlotDTO();
		slotDTO.setPatientID("invalid");
		slotDTO.setDoctorID(doctorRepo.findAll().get(0).getId());
		service.findSlot(slotDTO);
	}

	@Test
	/**
	 * Pass in valid data (broad search criteria) should find something
	 * 
	 * @throws ServiceException
	 */
	public void test_shouldFindSlot() throws ServiceException {
		OPSlotDTO slotDTO = new OPSlotDTO();
		slotDTO.setPatientID(patientRepo.findAll().get(0).getId());
		slotDTO.setDoctorID(doctorRepo.findAll().get(0).getId());
		slotDTO.setDistance(1000);
		slotDTO.setFrom(new Date(0));
		slotDTO.setTo(new Date(1590969600000L)); // 01.06.2020
		slotDTO.setType(OperationType.cardio);
		OPSlot result = service.findSlot(slotDTO);
		assertNotNull(result);
	}
}
