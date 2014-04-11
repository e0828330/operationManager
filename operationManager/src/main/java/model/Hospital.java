package model;

import java.io.Serializable;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.Point;

@Data
public class Hospital implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String name;
	
	private Point position;
}
