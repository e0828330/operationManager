package repository;

import model.Doctor;
import model.Patient;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorRepository extends MongoRepository<Doctor, String>  {
	public Doctor findByUsernameAndPassword(String username, String password);
}
