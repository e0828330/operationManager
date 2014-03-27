package model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Hospital implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
}
