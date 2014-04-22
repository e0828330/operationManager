package model;

import java.io.Serializable;
import java.util.Date;



import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class OPSlot implements Serializable {
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
