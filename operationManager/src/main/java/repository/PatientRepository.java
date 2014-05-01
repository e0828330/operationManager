package repository;

import model.Patient;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientRepository extends MongoRepository<Patient, String> {
	public Patient findByUsernameAndPassword(String username, String password);
}
