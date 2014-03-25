package model;

import lombok.Data;

import org.springframework.data.annotation.Id;

@Data
public class Patient {
	@Id
	private String id;

	private String firstName;
	private String lastName;
}
