package service.mock;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import service.IQueueService;

@Service
public class MockedQueueService {

	@Bean
	static IQueueService getQueueService() {
		IQueueService mock =  Mockito.mock(IQueueService.class);		
		return mock;
	}


}
