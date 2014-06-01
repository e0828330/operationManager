package model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import model.NotificationType;

@Data
@EqualsAndHashCode(callSuper=true)
public class NotificationDTO extends Message {

	private static final long serialVersionUID = 1L;

	private String opSlotID;
	
	private NotificationType type;

	private String recipientID;
	
	private String message;
}
