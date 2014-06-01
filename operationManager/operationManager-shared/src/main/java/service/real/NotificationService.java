package service.real;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Notification;
import model.User;
import repository.NotificationRepository;
import service.INotificationService;

@Service
public class NotificationService implements INotificationService {

	@Autowired
	private NotificationRepository notificationRepo;
	
	@Override
	public void save(Notification notification) {
		notificationRepo.save(notification);
	}

	@Override
	public List<Notification> getForUser(User user) {
		return notificationRepo.findByUserId(user.getId());
	}

}
