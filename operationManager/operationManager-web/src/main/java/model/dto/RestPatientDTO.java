package model.dto;

import java.io.Serializable;

import lombok.Data;
import model.Patient;

@Data
public class RestPatientDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8523849840056533437L;

	private String id;
	
	private String firstName;
	private String lastName;
		
	public RestPatientDTO() {
		
	}
	
	public RestPatientDTO(Patient patient) {
		id = patient.getId();
		firstName = patient.getFirstName();
		lastName = patient.getLastName();
	}
}
