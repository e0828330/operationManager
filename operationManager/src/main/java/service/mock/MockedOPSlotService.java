package service.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import service.IOPSlotService;
import model.OPSlot;

@Service
public class MockedOPSlotService {

	@Bean
	static IOPSlotService getOPSlotService() {
		IOPSlotService mock = Mockito.mock(IOPSlotService.class);

		Mockito.when(mock.getOPSlots(Mockito.anyLong(), Mockito.anyLong())).thenAnswer(new Answer<List<OPSlot>>() {

			@Override
			public List<OPSlot> answer(InvocationOnMock invocation)
					throws Throwable {
				List<OPSlot> opSlots = new ArrayList<OPSlot>();
				
				OPSlot slot1 = new OPSlot();
				slot1.setDate(new Date());
				opSlots.add(slot1);
				
				return opSlots;
			}
			
		});
		
		Mockito.when(mock.getOPSlotCount()).thenReturn(1L);
		
		return mock;
	}

}
