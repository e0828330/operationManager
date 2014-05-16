package model.dto;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import model.OperationType;
import model.Patient;

@Data
@EqualsAndHashCode(callSuper=true)
public class OPSlotDTO extends Message {

	private static final long serialVersionUID = 1L;
	
	private Date date;
	private OperationType type;
	private Patient patient;
	

}
