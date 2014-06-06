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
import model.User;
import model.dto.OPSlotFilter;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import service.IOPSlotService;

@Service
public class MockedOPSlotService {

	@Bean
	static IOPSlotService getOPSlotService() {
		IOPSlotService mock = Mockito.mock(IOPSlotService.class);
		
		final List<OPSlot> first = new ArrayList<OPSlot>();
		final List<OPSlot> second = new ArrayList<OPSlot>();
		final List<OPSlot> third = new ArrayList<OPSlot>();
		
		first.add(getMockedOPSlot("1", OperationType.eye, "SMZ", "Dr. Augfehler", "Adelheid", "Abesser", OperationStatus.reserved));
		
		second.add(getMockedOPSlot("2", OperationType.eye, "SMZ", "Dr. Augfehler", "Adelheid", "Abesser", OperationStatus.reserved));
		second.add(getMockedOPSlot("3", OperationType.ortho, "LK-K", null, null, null, OperationStatus.free));
		
		third.add(getMockedOPSlot("4", OperationType.eye, "SMZ", "Dr. Augfehler", "Adelheid", "Abesser", OperationStatus.reserved));
		third.add(getMockedOPSlot("5", OperationType.ortho, "LK-K", null, null, null, OperationStatus.free));
		third.add(getMockedOPSlot("6", OperationType.cardio, "LK-B", "Dr. Morks", "Gloria", "Geraus", OperationStatus.reserved));

		//return different lists the first 3 calls, so the AjaxSelfUpdatingTimerBehavior can be tested
		Mockito.when(mock.getOPSlots(Mockito.any(User.class), Mockito.any(Sort.class), Mockito.any(OPSlotFilter.class), Mockito.anyLong(), Mockito.anyLong())).thenReturn(first).thenReturn(second).thenReturn(third);
		
		Mockito.when(mock.getOPSlotCount(Mockito.any(User.class), Mockito.any(OPSlotFilter.class))).thenReturn(1L).thenReturn(2L).thenReturn(3L);
		
		Mockito.when(mock.getById(Mockito.anyString())).thenAnswer(new Answer<OPSlot>() {
			@Override
			public OPSlot answer(InvocationOnMock invocation) throws Throwable {
				String id = (String)invocation.getArguments()[0];
				for (OPSlot s : first) {
					if (s.getId().equals(id)) {
						return s;
					}
				}
				for (OPSlot s : second) {
					if (s.getId().equals(id)) {
						return s;
					}
				}
				for (OPSlot s : third) {
					if (s.getId().equals(id)) {
						return s;
					}
				}
				
				return null;
			}
		});
		
		return mock;
	}
	
	public static OPSlot getMockedOPSlot(String id, OperationType type, String hospitalName, String doctorName, String patientFirstName, String patientLastName, OperationStatus status) {
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
		slot.setId(id);
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
