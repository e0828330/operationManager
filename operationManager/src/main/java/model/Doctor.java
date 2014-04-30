package model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;

@Data
@EqualsAndHashCode(callSuper=true)
public class Doctor extends User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;

	private String firstName;
	private String lastName;
}
