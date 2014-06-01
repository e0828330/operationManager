package model;

import java.io.Serializable;

import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Document
@EqualsAndHashCode(callSuper=true)
public class Patient extends User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5300740584902282823L;

	private String firstName;
	private String lastName;
	
	@GeoSpatialIndexed
	private Point position;
}
