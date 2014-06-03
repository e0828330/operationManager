package model.dto;

import java.io.Serializable;

import lombok.Data;
import model.Doctor;

@Data
public class RestDoctorDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1000354263809336484L;

	private String id;

	private String firstName;
	private String lastName;

	public RestDoctorDTO() {

	}

	public RestDoctorDTO(Doctor doctor) {
		id = doctor.getId();
		firstName = doctor.getFirstName();
		lastName = doctor.getLastName();
	}
}
