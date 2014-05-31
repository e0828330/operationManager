package service;

import java.util.List;

import model.OPSlot;
import model.User;
import model.dto.OPSlotDTO;
import model.dto.OPSlotFilter;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;

public interface IOPSlotService {

	/**
	 * returns a list of OP slots
	 * 
	 * @param user the current user
	 * @param sort the sort parameter
	 * @param filter a filter object to narrow the results
	 * @param page the page of results to display
	 * @param itemsPerPage the number of elements to include in a page
	 * @return
	 */
	public List<OPSlot> getOPSlots(User user, SortParam<String> sort, OPSlotFilter filter, long page, long itemsPerPage);
	
	/**
	 * returns the number of found OP slots
	 * 
	 * @param user the current user
	 * @param filter a filter object to narrow the results
	 * @return
	 */
	public long getOPSlotCount(User user, OPSlotFilter filter);
	
	
	/**
	 * Saves the passed in OP slot
	 * @param slot
	 */
	public void saveOPSlot(OPSlot slot);
	
	/**
	 * Sends the given slot to the georesolver queue for reservation
	 * 
	 * @param slot
	 */
	public void reserveOPSlot(OPSlotDTO slot);

	/**
	 * Deletes the given slot from the database
	 * @param slot
	 */
	public void deleteOPSlot(OPSlot slot);
	
}
