package model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import model.NotificationType;
import model.OPSlot;

@Data
@EqualsAndHashCode(callSuper=true)
public class NotificationDTO extends Message {

	private static final long serialVersionUID = 1L;

	private OPSlot slot;
	
	private NotificationType type;

	private String userID;
}
