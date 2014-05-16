package model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class RestErrorDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 849663543214414795L;

	private String error;
}
