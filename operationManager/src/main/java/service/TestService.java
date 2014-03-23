package service;

import model.Patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.PatientRepository;

@Service
public class TestService {
	
	@Autowired
	private PatientRepository patientRepo;
	
	public String getString() {
		String result = "";
		for (Patient p : patientRepo.findAll()) {
			result += "Patient: " + p.getName() + "\n";
		}
		return result;
	}
	
	public void savePatient() {
		Patient p = new Patient();
		p.setName("Tester");
		patientRepo.save(p);
	}
}
