package model.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import model.Notification;
import model.NotificationType;

@Data
public class RestNotificationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8393876726799798943L;

	private String id;
	
	private Date timestamp;

	private RestOPSlotDTO slot;
	
	private NotificationType type;

	private String message;
	
	public RestNotificationDTO() {
		
	}
	
	public RestNotificationDTO(Notification notification) {
		id = notification.getId();
		timestamp = notification.getTimestamp();
		type = notification.getType();
		message = notification.getMessage();

		if (notification.getSlot() != null) {
			slot = new RestOPSlotDTO(notification.getSlot());
		}
	}
}
