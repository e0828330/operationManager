package model;

import java.util.Date;

import lombok.Data;
import model.dto.Message;

import org.springframework.data.annotation.Id;

@Data
public class OPSlot extends Message {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private Date date;
	private Date from;
	private Date to;
	private OperationType type;
	private Hospital hospital;
	private Doctor doctor;
	private OperationStatus status;
	private Patient patient;
}
