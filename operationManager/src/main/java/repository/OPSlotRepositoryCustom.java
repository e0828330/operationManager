package repository;

import java.util.Date;

import model.OPSlot;
import model.OperationType;

import org.springframework.data.mongodb.core.geo.Distance;
import org.springframework.data.mongodb.core.geo.Point;

public interface OPSlotRepositoryCustom {
	/**
	 * Returns the best (as in nearest in time and position) for the given parameters
	 * 
	 * @param point
	 * @param distance
	 * @param from
	 * @param to
	 * @return
	 */
	public OPSlot findBestInRange(Point point, Distance distance, OperationType type, Date from, Date to);
}
