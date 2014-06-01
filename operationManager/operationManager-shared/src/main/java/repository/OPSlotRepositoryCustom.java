package repository;

import java.util.Date;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

import model.OPSlot;
import model.OperationType;



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
