package model;

public class Point extends org.springframework.data.mongodb.core.geo.Point {

	public Point() {
		super(0, 0);
	}
	
	public Point(org.springframework.data.mongodb.core.geo.Point point) {
		super(point);
	}

	public Point(double x, double y) {
		super(x, y);
	}

}
