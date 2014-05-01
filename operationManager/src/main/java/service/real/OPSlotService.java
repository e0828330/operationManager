package service.real;

import java.util.List;

import model.OPSlot;
import model.dto.OPSlotFilter;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.OPSlotRepository;
import service.IOPSlotService;

@Service
public class OPSlotService implements IOPSlotService {
	
	@Autowired
	private OPSlotRepository repo;
	
	@Override
	public List<OPSlot> getOPSlots(SortParam<String> sort, OPSlotFilter filter, long first, long count) {
		System.out.println(filter.toString());
		
		String status = "";
		String type = "";
		
		if (filter.getPatient() == null) {
			filter.setPatient("");
		}
		if (filter.getHospital() == null) {
			filter.setHospital("");
		}
		if (filter.getDoctor() == null) {
			filter.setDoctor("");
		}
		if (filter.getStatus() != null) {
			status = filter.getStatus().name();
		}
		if (filter.getType() != null) {
			type = filter.getType().name();
		}
		
		
		return (List<OPSlot>) repo.findByFilter(filter.getPatient(), filter.getHospital(), filter.getDoctor(), status, type);
	}

	@Override
	public long getOPSlotCount(OPSlotFilter filter) {

		String status = "";
		String type = "";
		
		if (filter.getPatient() == null) {
			filter.setPatient("");
		}
		if (filter.getHospital() == null) {
			filter.setHospital("");
		}
		if (filter.getDoctor() == null) {
			filter.setDoctor("");
		}
		if (filter.getStatus() != null) {
			status = filter.getStatus().name();
		}
		if (filter.getType() != null) {
			type = filter.getType().name();
		}
		
		return repo.countByFilter(filter.getPatient(), filter.getHospital(), filter.getDoctor(), status, type);
	}

	@Override
	public void saveOPSlot(OPSlot slot) {
		repo.save(slot);
	}

	@Override
	public void reserveOPSlot(OPSlot slot) {
		// TODO Auto-generated method stub
	}
}
