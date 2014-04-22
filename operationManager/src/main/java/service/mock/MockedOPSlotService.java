package service.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import service.IOPSlotService;
import model.Doctor;
import model.Hospital;
import model.OPSlot;
import model.OperationStatus;
import model.OperationType;
import model.Patient;

@Service
public class MockedOPSlotService {

	@Bean
	static IOPSlotService getOPSlotService() {
		IOPSlotService mock = Mockito.mock(IOPSlotService.class);

		Mockito.when(mock.getOPSlots(Mockito.any(SortParam.class), Mockito.anyLong(), Mockito.anyLong())).thenAnswer(new Answer<List<OPSlot>>() {

			@Override
			public List<OPSlot> answer(InvocationOnMock invocation)
					throws Throwable {
				List<OPSlot> opSlots = new ArrayList<OPSlot>();
				
				opSlots.add(getMockedOPSlot(OperationType.eye, "SMZ", "Dr. Augfehler", "Adelheid", "Abesser", OperationStatus.reserved));
				opSlots.add(getMockedOPSlot(OperationType.ortho, "LK-K", null, null, null, OperationStatus.free));
				opSlots.add(getMockedOPSlot(OperationType.cardio, "LK-B", "Dr. Morks", "Gloria", "Geraus", OperationStatus.reserved));
				
				return opSlots;
			}
			
		});
		
		Mockito.when(mock.getOPSlotCount()).thenReturn(3L);
		
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
			doctor.setName(doctorName);
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
