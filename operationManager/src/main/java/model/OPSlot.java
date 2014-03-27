package model;

import java.io.Serializable;
import java.util.Date;


import lombok.Data;

@Data
public class OPSlot implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date date;
	private OperationType type;
	private Hospital hospital;
	private Doctor doctor;
	private OperationStatus status;
}
