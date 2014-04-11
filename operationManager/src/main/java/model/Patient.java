package model;

import java.io.Serializable;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.Point;

@Data
public class Patient implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5300740584902282823L;

	@Id
	private String id;

	private String firstName;
	private String lastName;
	
	private Point position;
}
