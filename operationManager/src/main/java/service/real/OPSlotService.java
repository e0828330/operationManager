package service.real;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import lombok.Data;
import model.Doctor;
import model.Hospital;
import model.OPSlot;
import model.OperationStatus;
import model.Patient;
import model.User;
import model.dto.OPSlotDTO;
import model.dto.OPSlotFilter;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import repository.OPSlotRepository;
import service.IOPSlotService;
import service.IQueueService;

@Service
public class OPSlotService implements IOPSlotService {

	@Autowired
	private OPSlotRepository repo;
	
	@Autowired
	private IQueueService queueService;

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

		private String dateMin;
		private String dateMax;

		private Integer fromMinute;
		private Integer toMinute;

		public FilterParams(OPSlotFilter filter) {
			status = "";
			type = "";

			if (filter.getPatient() == null) {
				setPatient("");
			} else {
				setPatient(filter.getPatient());
			}

			if (filter.getHospital() == null) {
				setHospital("");
			} else {
				setHospital(filter.getHospital());
			}

			if (filter.getDoctor() == null) {
				setDoctor("");
			} else {
				setDoctor(filter.getDoctor());
			}

			if (filter.getStatus() != null) {
				status = filter.getStatus().name();
			}
			if (filter.getType() != null) {
				type = filter.getType().name();
			}

			// Handle dates
			Calendar cal = Calendar.getInstance();

			if (filter.getDate() != null) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				setDateMin(df.format(filter.getDate()));

				cal.setTime(filter.getDate());
				cal.add(Calendar.DATE, 1);
				setDateMax(df.format(cal.getTime()));
			}

			// Convert time to number of minutes
			if (filter.getFrom() != null) {
				cal.setTime(filter.getFrom());
				setFromMinute(cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE));
			}

			// Convert time to number of minutes
			if (filter.getTo() != null) {
				cal.setTime(filter.getTo());
				setToMinute(cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE));
			}
		}
	}

	@Override
	public List<OPSlot> getOPSlots(User user, SortParam<String> sort, OPSlotFilter filter, long page, long itemsPerPage) {
		FilterParams filterParams = new FilterParams(filter);

		/* Sort and paging */
		PageRequest pager;
		if (sort != null) {
			Sort sorter = new Sort(sort.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC, sort.getProperty());
			pager = new PageRequest((int) page, (int) itemsPerPage, sorter);
		} else {
			pager = new PageRequest((int) page, (int) itemsPerPage);
		}

		List<OPSlot> result = null;

		/* Call the correct method based on the user's role */
		switch (user.getRole()) {
		case DEFAULT:
			result = (List<OPSlot>) repo.findByFilter(filterParams.getHospital(), filterParams.getDoctor(), filterParams.getStatus(), filterParams.getType(),
					filterParams.getDateMin(), filterParams.getDateMax(), filterParams.getFromMinute(), filterParams.getToMinute(), pager);
			break;
		case PATIENT:
			result = (List<OPSlot>) repo.findByFilterForPatient(((Patient) user).getId(), filterParams.getHospital(), filterParams.getDoctor(), filterParams.getStatus(), filterParams.getType(),
							filterParams.getDateMin(), filterParams.getDateMax(), filterParams.getFromMinute(), filterParams.getToMinute(), pager);
		case DOCTOR:
			result = (List<OPSlot>) repo.findByFilterForDoctor(((Doctor) user).getId(), filterParams.getPatient(), filterParams.getHospital(), filterParams.getStatus(), filterParams.getType(),
							filterParams.getDateMin(), filterParams.getDateMax(), filterParams.getFromMinute(), filterParams.getToMinute(), pager);
			break;
		case HOSPITAL:
			result = (List<OPSlot>) repo.findByFilterForHospital(((Hospital) user).getId(), filterParams.getPatient(), filterParams.getDoctor(), filterParams.getStatus(), filterParams.getType(),
					filterParams.getDateMin(), filterParams.getDateMax(), filterParams.getFromMinute(), filterParams.getToMinute(), pager);
		}
		

		return result;
	}

	@Override
	public long getOPSlotCount(User user, OPSlotFilter filter) {
		FilterParams filterParams = new FilterParams(filter);
		long result = 0;

		/* Call the correct method based on the user's role */
		switch (user.getRole()) {
		case DEFAULT:
			result = repo.countByFilter(filterParams.getHospital(), filterParams.getDoctor(), filterParams.getStatus(), filterParams.getType(),
					filterParams.getDateMin(), filterParams.getDateMax(), filterParams.getFromMinute(), filterParams.getToMinute());
			break;
		case PATIENT:
			result = repo.countByFilterForPatient(((Patient) user).getId(), filterParams.getHospital(), filterParams.getDoctor(), filterParams.getStatus(), filterParams.getType(),
							filterParams.getDateMin(), filterParams.getDateMax(), filterParams.getFromMinute(), filterParams.getToMinute());
		case DOCTOR:
			result = repo.countByFilterForDoctor(((Doctor) user).getId(), filterParams.getPatient(), filterParams.getHospital(), filterParams.getStatus(), filterParams.getType(),
							filterParams.getDateMin(), filterParams.getDateMax(), filterParams.getFromMinute(), filterParams.getToMinute());
			break;
		case HOSPITAL:
			result = repo.countByFilterForHospital(((Hospital) user).getId(), filterParams.getPatient(), filterParams.getDoctor(), filterParams.getStatus(), filterParams.getType(),
					filterParams.getDateMin(), filterParams.getDateMax(), filterParams.getFromMinute(), filterParams.getToMinute());		
		}
		
		return result;
	}

	@Override
	public void saveOPSlot(OPSlot slot) {
		repo.save(slot);
	}

	@Override
	public void reserveOPSlot(OPSlotDTO slot) {
		queueService.sendToGeoResolver(slot);
	}

	@Override
	public void deleteOPSlot(OPSlot slot) {
		repo.delete(slot);
	}

	@Override
	public void cancelReservation(OPSlot slot) {
		slot.setDoctor(null);
		slot.setPatient(null);
		slot.setStatus(OperationStatus.free);
		repo.save(slot);
	}

	@Override
	public OPSlot getById(String id) {
		return repo.findOne(id);
	}
}
