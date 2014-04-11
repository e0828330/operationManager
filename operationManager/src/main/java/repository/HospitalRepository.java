package repository;

import java.util.List;

import model.Hospital;

import org.springframework.data.mongodb.core.geo.Distance;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HospitalRepository  extends MongoRepository<Hospital, String> {
	 List<Hospital> findByPositionNear(Point p, Distance d);
}
