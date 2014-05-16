package model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.Data;
import model.dto.Message;

@Data
public class Notification extends Message {

	private static final long serialVersionUID = 3605958261067049387L;

	@Id
	private String id;

	private OPSlot slot;
	
	private NotificationType type;

	private User recipient;
}
