package model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import org.springframework.data.annotation.Id;

@Data
public class Notification implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private Date timestamp;

	private OPSlot slot;
	
	private NotificationType type;

	private User recipient;
	
	private String message;
}
