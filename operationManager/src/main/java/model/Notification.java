package model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Notification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5350513499526902081L;

	@Id
	private String id;

	private OPSlot slot;
	
	private NotificationType type;

	private User recipient;
}
