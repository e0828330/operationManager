package model.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import model.OperationStatus;
import model.OperationType;

@Data
public class OPSlotFilter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date date;
	private Date from;
	private Date to;
	private String patient;
	private String hospital;
	private String doctor;
	private OperationType type;
	private OperationStatus status;
}
