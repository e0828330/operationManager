package repository;

import java.util.List;

import model.Notification;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface NotificationRepository extends MongoRepository<Notification, String> {

		@Query("{ 'recipent.id' : ?0 }")
		public List<Notification> findByUserId(String id);
}
