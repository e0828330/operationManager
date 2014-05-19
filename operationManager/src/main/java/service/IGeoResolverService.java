package service;

import model.OPSlot;
import model.dto.OPSlotDTO;

public interface IGeoResolverService {
	/**
	 * Finds a free slot in the database that matches the given params
	 * passed in via the opslot dto object
	 *
	 * @param params
	 * @return
	 */
	public OPSlot findSlot(OPSlotDTO params);
}
