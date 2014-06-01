package repository;

import java.util.List;

import model.Patient;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PatientRepository extends MongoRepository<Patient, String> {
	public Patient findByUsernameAndPassword(String username, String password);


	@Query("{ $or : ["
			+ "	{'firstName' : {$regex : ?0, $options: 'i'} }, "
			+ "	{'lastName' : {$regex : ?0, $options: 'i'} }"
			+ " ] }")
	public List<Patient> findByKeyword(String keyword);
}
