package service;

import java.util.List;

import model.Notification;
import model.User;

public interface INotificationService {
	/**
	 * Stores the given notification into the database
	 * 
	 * @param notification
	 */
	public void save(Notification notification);
	
	/**
	 * Gets all notifications for a given user
	 * 
	 * @param user
	 * @return
	 */
	public List<Notification> getForUser(User user);
}
