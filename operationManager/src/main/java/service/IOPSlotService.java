package service;

import java.util.List;

import model.OPSlot;
import model.dto.OPSlotFilter;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;

public interface IOPSlotService {

	/**
	 * returns a list of OP slots
	 * @param sort the sort parameter
	 * @param filter a filter object to narrow the results
	 * @param first the offset for the list
	 * @param count the number of elements to return
	 * @return
	 */
	List<OPSlot> getOPSlots(SortParam<String> sort, OPSlotFilter filter, long first, long count);
	
	/**
	 * returns the number of found OP slots
	 * @param filter a filter object to narrow the results
	 * @return
	 */
	long getOPSlotCount(OPSlotFilter filter);

}
