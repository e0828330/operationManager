package repository;

import model.Patient;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PatientRepository extends MongoRepository<Patient, String> {
	public Patient findByUsernameAndPassword(String username, String password);
}
