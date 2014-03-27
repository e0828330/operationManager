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
				
				opSlots.add(getMockedOPSlot(OperationType.eye, "SMZ", "Dr. Augfehler", OperationStatus.reserved));
				opSlots.add(getMockedOPSlot(OperationType.ortho, "LK-K", "-", OperationStatus.free));
				opSlots.add(getMockedOPSlot(OperationType.cardio, "LK-B", "Dr. Morks", OperationStatus.reserved));
				
				return opSlots;
			}
			
		});
		
		Mockito.when(mock.getOPSlotCount()).thenReturn(3L);
		
		return mock;
	}
	
	private static OPSlot getMockedOPSlot(OperationType type, String hospitalName, String doctorName, OperationStatus status) {
		Hospital hospital = new Hospital();
		hospital.setName(hospitalName);
		
		Doctor doctor = new Doctor();
		doctor.setName(doctorName);
		
		OPSlot slot = new OPSlot();
		slot.setDate(new Date());
		slot.setDoctor(doctor);
		slot.setHospital(hospital);
		slot.setStatus(status);
		slot.setType(type);
		
		return slot;
	}

}
