package model.dto;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import model.OperationType;

@Data
@EqualsAndHashCode(callSuper=true)
public class OPSlotDTO extends Message {

	private static final long serialVersionUID = 1L;
	
	private String patientID;
	private Date from;
	private Date to;
	private OperationType type;
	private int distance;

	
}
