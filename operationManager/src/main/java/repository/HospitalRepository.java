package repository;

import java.util.List;

import model.Hospital;
import model.Patient;

import org.springframework.data.mongodb.core.geo.Distance;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HospitalRepository  extends MongoRepository<Hospital, String> {
	 public List<Hospital> findByPositionNear(Point p, Distance d);
	 public Hospital findByUsernameAndPassword(String username, String password);
}
