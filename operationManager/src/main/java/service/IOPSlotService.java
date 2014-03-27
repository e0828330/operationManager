package service;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;

import model.OPSlot;

public interface IOPSlotService {

	List<OPSlot> getOPSlots(SortParam<String> sort, long first, long count);
	
	long getOPSlotCount();

}
