package service.real;

import java.util.List;

import model.OPSlot;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.springframework.stereotype.Service;

import service.IOPSlotService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Service
public class OPSlotService implements IOPSlotService {
	
	@Override
	public List<OPSlot> getOPSlots(SortParam<String> sort, long first, long count) {
		throw new NotImplementedException();
	}

	@Override
	public long getOPSlotCount() {
		throw new NotImplementedException();
	}
}
