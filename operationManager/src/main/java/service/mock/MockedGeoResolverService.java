package service.mock;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import service.IGeoResolverService;

@Service
public class MockedGeoResolverService {
	@Bean
	static IGeoResolverService getGeoResolverService() {
		IGeoResolverService mock =  Mockito.mock(IGeoResolverService.class);
		return mock;
	}
}
