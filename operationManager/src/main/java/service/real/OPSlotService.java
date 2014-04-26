package service.real;

import java.util.List;

import model.OPSlot;
import model.dto.OPSlotFilter;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.springframework.stereotype.Service;

import service.IOPSlotService;

@Service
public class OPSlotService implements IOPSlotService {
	
	@Override
	public List<OPSlot> getOPSlots(SortParam<String> sort, OPSlotFilter filter, long first, long count) {
		return null;
	}

	@Override
	public long getOPSlotCount(OPSlotFilter filter) {
		return 0L;
	}
}
