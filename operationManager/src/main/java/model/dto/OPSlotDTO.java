package model.dto;

import java.util.Date;

import lombok.Data;
import model.OperationType;
import model.Patient;

import org.springframework.data.annotation.Id;

@Data
public class OPSlotDTO extends MessageDTO {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private Date date;
	private OperationType type;
	private Patient patient;
	

}
