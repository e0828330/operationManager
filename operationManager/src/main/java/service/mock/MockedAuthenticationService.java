package service.mock;

import model.Role;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;

import service.IAuthenticationService;

public class MockedAuthenticationService {

	@Bean
	static IAuthenticationService getAuthenticationService() {
		IAuthenticationService mock = Mockito.mock(IAuthenticationService.class);
		
		Mockito.when(mock.authenticate(Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Role>() {

			@Override
			public Role answer(InvocationOnMock invocation) throws Throwable {
				String username = (String)invocation.getArguments()[0];
				
				if (username.equals("patient")) {
					return Role.PATIENT;
				}
				else if (username.equals("patient")) {
					return Role.PATIENT;
				}
				else if (username.equals("patient")) {
					return Role.PATIENT;
				}
				return Role.DEFAULT;
			}
			
		});
		
		return mock;
	}
}
