package service.real;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Patient;
import repository.PatientRepository;
import service.IPatientService;

@Service
public class PatientService implements IPatientService {

	@Autowired
	private PatientRepository patientRepo;
	
	@Override
	public List<Patient> getPatients() {
		return patientRepo.findAll();
	}

	@Override
	public List<Patient> getPatients(String keyword) {
		return patientRepo.findByKeyword(keyword);
	}

}
