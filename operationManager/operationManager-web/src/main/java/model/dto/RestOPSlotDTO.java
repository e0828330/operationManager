package model.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import model.OPSlot;
import model.OperationStatus;
import model.OperationType;

@Data
public class RestOPSlotDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2642299454664334795L;


	private String id;
	
	private Date date;
	private Date from;
	private Date to;
	private OperationType type;
	private RestHospitalDTO hospital;
	private RestDoctorDTO doctor;
	private OperationStatus status;
	private RestPatientDTO patient;
	
	public RestOPSlotDTO() {
		
	}
	
	public RestOPSlotDTO(OPSlot slot) {
		id = slot.getId();
		date = slot.getDate();
		to = slot.getTo();
		type = slot.getType();
		status = slot.getStatus();
		
		if (slot.getHospital() != null) {
			hospital = new RestHospitalDTO(slot.getHospital());
		}

		if (slot.getDoctor() != null) {
			doctor = new RestDoctorDTO(slot.getDoctor());
		}
		
		if (slot.getPatient() != null) {
			patient = new RestPatientDTO(slot.getPatient());
		}
	}
}
