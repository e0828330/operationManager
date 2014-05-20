package model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class Doctor extends User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
}
