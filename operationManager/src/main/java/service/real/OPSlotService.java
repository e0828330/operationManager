package service.real;

import java.util.Date;
import java.util.List;

import model.Doctor;
import model.Hospital;
import model.OPSlot;
import model.OperationStatus;
import model.OperationType;
import model.Patient;
import model.dto.OPSlotFilter;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import repository.OPSlotRepository;
import service.IOPSlotService;

@Service
public class OPSlotService implements IOPSlotService {
	
	@Autowired
	private OPSlotRepository repo;

	
	@Override
	public List<OPSlot> getOPSlots(SortParam<String> sort, OPSlotFilter filter, long first, long count) {
		return null;
	}

	@Override
	public long getOPSlotCount(OPSlotFilter filter) {
		/*
		OPSlot slot = new OPSlot();
		
		Patient patient = new Patient();
		patient.setFirstName("Adelheid");
		patient.setLastName("Abesser");
		slot.setPatient(patient);
		
		Hospital hs = new Hospital();
		hs.setName("SMZ Ost");
		slot.setHospital(hs);

		slot.setDate(new Date());
		slot.setFrom(new Date());
		slot.setTo(new Date(new Date().getTime() + 3600000));
		slot.setType(OperationType.eye);
		slot.setStatus(OperationStatus.reserved);
		
		repo.save(slot);*/
			
		return 0L;
	}
}
