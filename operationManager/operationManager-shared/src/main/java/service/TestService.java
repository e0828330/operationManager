package service;

import java.util.ArrayList;
import java.util.List;

import model.Patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.PatientRepository;

@Service
public class TestService {
	
	@Autowired
	private PatientRepository patientRepo;
	
	/**
	 * Returns a list of patients
	 * @return
	 */
	public List<Patient> getPatients() {
		ArrayList<Patient> result = new ArrayList<>();
		for (Patient p : patientRepo.findAll()) {
			result.add(p);
		}

		return result;
	}
	
	/**
	 * Saves patient to the database
	 * 
	 * @param firstName
	 * @param lastName
	 */
	public void addPatient(String firstName, String lastName) {
		Patient p = new Patient();
//		p.setFirstName(firstName);
//		p.setLastName(lastName);
		patientRepo.save(p);
	}
}
