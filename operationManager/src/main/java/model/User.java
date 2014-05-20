package model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class User implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7357377694702848930L;
	
	@Id
	private String id;
	
	private String username;
	private String password;
	protected Role role;
}
