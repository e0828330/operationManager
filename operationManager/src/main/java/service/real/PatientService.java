package service.real;

import java.util.List;

import model.Patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	@Override
	public Patient getById(String id) {
		return patientRepo.findOne(id);
	}

}
