package service.real;

import java.util.List;

import lombok.Data;
import model.OPSlot;
import model.dto.OPSlotFilter;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import repository.OPSlotRepository;
import service.IOPSlotService;

@Service
public class OPSlotService implements IOPSlotService {
	
	@Autowired
	private OPSlotRepository repo;
	
	@Data
	/**
	 * Used to avoid having to duplicate the param parsing
	 */
	private class FilterParams {
		private String patient;
		private String hospital;
		private String doctor;
		private String status;
		private String type;

		public FilterParams(OPSlotFilter filter) {
			status = "";
			type = "";
			
			System.err.println(filter);

			if (filter.getPatient() == null) {
				setPatient("");
			}
			if (filter.getHospital() == null) {
				setHospital("");
			}
			if (filter.getDoctor() == null) {
				setDoctor("");
			}
			if (filter.getStatus() != null) {
				status = filter.getStatus().name();
			}
			if (filter.getType() != null) {
				type = filter.getType().name();
			}
		}
	}
	
	@Override
	public List<OPSlot> getOPSlots(SortParam<String> sort, OPSlotFilter filter, long page, long itemsPerPage) {		
		FilterParams filterParams = new FilterParams(filter);

		/* Sort and paging */
		PageRequest pager;
		if (sort != null) {
			Sort sorter = new Sort(sort.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC, sort.getProperty());
			pager = new PageRequest((int)page, (int)itemsPerPage, sorter);
		}
		else {
			pager = new PageRequest((int)page, (int)itemsPerPage);
		}
		
		return (List<OPSlot>) repo.findByFilter(filterParams.getPatient(), filterParams.getHospital(), filterParams.getDoctor(),
												filterParams.getStatus(), filterParams.getType(), pager);
	}

	@Override
	public long getOPSlotCount(OPSlotFilter filter) {
		FilterParams filterParams = new FilterParams(filter);
		
		return repo.countByFilter(filterParams.getPatient(), filterParams.getHospital(), filterParams.getDoctor(),
								  filterParams.getStatus(), filterParams.getType());
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
