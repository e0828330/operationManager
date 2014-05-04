package service.real;

import java.security.NoSuchAlgorithmException;

import model.Doctor;
import model.Hospital;
import model.Patient;
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
		try {
			// First look for patient
			Patient patient = this.patientRepo.findByUsernameAndPassword(username, Utils.computeHash(password));
			if (patient != null && patient.getRole() != null) {
				Patient p = new Patient();
				p.setPassword(patient.getPassword());
				p.setRole(patient.getRole());
				p.setUsername(patient.getUsername());
				p.setPosition(patient.getPosition());
				p.setLastName(patient.getLastName());
				p.setFirstName(patient.getFirstName());
				p.setId(patient.getId());
				return p;
			}
			
			// Look for doctor
			Doctor doctor = this.doctorRepo.findByUsernameAndPassword(username, Utils.computeHash(password));
			if (doctor != null && doctor.getRole() != null) {
				Doctor d = new Doctor();
				d.setPassword(doctor.getPassword());
				d.setRole(doctor.getRole());
				d.setUsername(doctor.getUsername());
				d.setFirstName(doctor.getFirstName());
				d.setLastName(d.getLastName());
				d.setId(d.getId());
				return d;
			}
			
			// Look for hospital
			Hospital hospital = this.hsRepo.findByUsernameAndPassword(username, Utils.computeHash(password));
			if (hospital != null && hospital.getRole() != null) {
				Hospital h = new Hospital();
				h.setPassword(hospital.getPassword());
				h.setRole(hospital.getRole());
				h.setUsername(hospital.getUsername());
				h.setPosition(hospital.getPosition());
				h.setName(hospital.getName());
				return h;
			}
			
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return null;
	}
	
}