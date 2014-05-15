package service.real;

import java.security.NoSuchAlgorithmException;

import model.Doctor;
import model.Hospital;
import model.Patient;
import model.Role;
import model.User;

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
	public User authenticate(String username, String password) {
		User noAuth = new User();
		noAuth.setRole(Role.DEFAULT);

		try {
			// First look for patient
			Patient patient = this.patientRepo.findByUsernameAndPassword(username, Utils.computeHash(password));
			if (patient != null && patient.getRole() != null) {
				return patient;
			}
			
			// Look for doctor
			Doctor doctor = this.doctorRepo.findByUsernameAndPassword(username, Utils.computeHash(password));
			if (doctor != null && doctor.getRole() != null) {
				return doctor;
			}
			
			// Look for hospital
			Hospital hospital = this.hsRepo.findByUsernameAndPassword(username, Utils.computeHash(password));
			if (hospital != null && hospital.getRole() != null) {
				return hospital;
			}
			
		} catch (NoSuchAlgorithmException e) {
			return noAuth;
		}
		return noAuth;
	}
	
}