package service;

import java.util.List;

import model.OPSlot;

public interface IOPSlotService {

	List<OPSlot> getOPSlots(long first, long count);
	
	long getOPSlotCount();

}
