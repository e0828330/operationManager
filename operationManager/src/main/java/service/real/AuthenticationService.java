package service.real;

import java.security.NoSuchAlgorithmException;

import model.Doctor;
import model.Hospital;
import model.Patient;
import model.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import repository.DoctorRepository;
import repository.HospitalRepository;
import repository.PatientRepository;
import service.IAuthenticationService;
import utils.Utils;

@Service
public class AuthenticationService implements IAuthenticationService {

	
	@Autowired 
	private PatientRepository patientRepo;
	
	@Autowired 
	private HospitalRepository hsRepo;
	
	@Autowired 
	private DoctorRepository doctorRepo;	
	
	@Bean
	static IAuthenticationService getAuthenticationService() {
		return new AuthenticationService();
	}	
	
	@Override
	public Role authenticate(String username, String password) {
		try {
			// First look for patient
			Patient patient = this.patientRepo.findByUsernameAndPassword(username, Utils.computeHash(password));
			if (patient != null && patient.getRole() != null) {
				return patient.getRole();
			}
			
			// Look for doctor
			Doctor doctor = this.doctorRepo.findByUsernameAndPassword(username, Utils.computeHash(password));
			if (doctor != null && doctor.getRole() != null) {
				return doctor.getRole();
			}
			
			// Look for hospital
			Hospital hospital = this.hsRepo.findByUsernameAndPassword(username, Utils.computeHash(password));
			if (hospital != null && doctor.getRole() != null) {
				return hospital.getRole();
			}
			
		} catch (NoSuchAlgorithmException e) {
			return Role.DEFAULT;
		}
		return Role.DEFAULT;
	}
	
}