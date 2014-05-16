package model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import model.Notification;

@Data
@EqualsAndHashCode(callSuper=true)
public class NotificationDTO extends Message {

	private static final long serialVersionUID = 1L;
	
	private Notification notification;
}
