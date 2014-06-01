package model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=true)
public class Patient extends User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5300740584902282823L;

	private String firstName;
	private String lastName;
	
	private transient Point position;
}
