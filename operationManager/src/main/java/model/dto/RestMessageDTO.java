package model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class RestMessageDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7147701922490921449L;
	private String message;
}
