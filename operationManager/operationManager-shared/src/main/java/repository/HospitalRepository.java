package repository;

import model.Hospital;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HospitalRepository  extends MongoRepository<Hospital, String> {
	 public Hospital findByUsernameAndPassword(String username, String password);
}
