package service.mock;

import java.util.ArrayList;
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
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import service.IOPSlotService;

@Service
public class MockedOPSlotService {

	@Bean
	static IOPSlotService getOPSlotService() {
		IOPSlotService mock = Mockito.mock(IOPSlotService.class);
		
		List<OPSlot> first = new ArrayList<OPSlot>();
		List<OPSlot> second = new ArrayList<OPSlot>();
		List<OPSlot> third = new ArrayList<OPSlot>();
		
		first.add(getMockedOPSlot(OperationType.eye, "SMZ", "Dr. Augfehler", "Adelheid", "Abesser", OperationStatus.reserved));
		
		second.add(getMockedOPSlot(OperationType.eye, "SMZ", "Dr. Augfehler", "Adelheid", "Abesser", OperationStatus.reserved));
		second.add(getMockedOPSlot(OperationType.ortho, "LK-K", null, null, null, OperationStatus.free));
		
		third.add(getMockedOPSlot(OperationType.eye, "SMZ", "Dr. Augfehler", "Adelheid", "Abesser", OperationStatus.reserved));
		third.add(getMockedOPSlot(OperationType.ortho, "LK-K", null, null, null, OperationStatus.free));
		third.add(getMockedOPSlot(OperationType.cardio, "LK-B", "Dr. Morks", "Gloria", "Geraus", OperationStatus.reserved));

		//return different lists the first 3 calls, so the AjaxSelfUpdatingTimerBehavior can be tested
		Mockito.when(mock.getOPSlots(Mockito.any(SortParam.class), Mockito.any(OPSlotFilter.class), Mockito.anyLong(), Mockito.anyLong())).thenReturn(first).thenReturn(second).thenReturn(third);
		
		Mockito.when(mock.getOPSlotCount(Mockito.any(OPSlotFilter.class))).thenReturn(1L).thenReturn(2L).thenReturn(3L);
		
		return mock;
	}
	
	private static OPSlot getMockedOPSlot(OperationType type, String hospitalName, String doctorName, String patientFirstName, String patientLastName, OperationStatus status) {
		Hospital hospital = null;
		Doctor doctor = null;
		Patient patient = null;
		
		if (hospitalName != null) {
			hospital = new Hospital();
			hospital.setName(hospitalName);
		}
		
		if (doctorName != null) {
			doctor = new Doctor();
			doctor.setLastName(doctorName);
		}
		
		if (patientFirstName != null) {
			patient = new Patient();
			patient.setFirstName(patientFirstName);
			patient.setLastName(patientLastName);
		}
		
		OPSlot slot = new OPSlot();
		slot.setDate(new Date());
		slot.setFrom(new Date());
		slot.setTo(new Date());
		slot.setDoctor(doctor);
		slot.setHospital(hospital);
		slot.setStatus(status);
		slot.setType(type);
		slot.setPatient(patient);
		
		return slot;
	}

}
