package model.dto;

import java.io.Serializable;

import lombok.Data;
import model.Hospital;

@Data
public class RestHospitalDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 326277302278566788L;
	private String id;
	private String name;
	
	public RestHospitalDTO() {

	}

	public RestHospitalDTO(Hospital hospital) {
		id = hospital.getId();
		name = hospital.getName();
	}
}
