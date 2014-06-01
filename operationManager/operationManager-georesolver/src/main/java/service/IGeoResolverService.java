package service;

import service.exceptions.ServiceException;
import model.OPSlot;
import model.dto.OPSlotDTO;

public interface IGeoResolverService {
	/**
	 * Finds a free slot in the database that matches the given params
	 * passed in via the opslot dto object
	 *
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public OPSlot findSlot(OPSlotDTO params) throws ServiceException;
}
