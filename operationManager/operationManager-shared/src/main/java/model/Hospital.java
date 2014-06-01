package model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;

@Data
@EqualsAndHashCode(callSuper=true)
public class Hospital extends User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	
	@GeoSpatialIndexed
	private transient Point position;
}
