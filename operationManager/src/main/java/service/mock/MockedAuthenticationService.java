package service.mock;

import model.Role;
import model.User;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import service.IAuthenticationService;

@Service
public class MockedAuthenticationService {

	@Bean
	static IAuthenticationService getAuthenticationService() {
		IAuthenticationService mock = Mockito.mock(IAuthenticationService.class);
		
		Mockito.when(mock.authenticate(Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<User>() {

			@Override
			public User answer(InvocationOnMock invocation) throws Throwable {
				String username = (String)invocation.getArguments()[0];
				User user = new User();
				user.setUsername(username);
				if (username.equals("patient")) {
					user.setRole(Role.PATIENT);
					return user;
				}
				else if (username.equals("doctor")) {
					user.setRole(Role.DOCTOR);
					return user;
				}
				else if (username.equals("hospital")) {
					user.setRole(Role.HOSPITAL);
					return user;
				}
				return null;
			}
			
		});
		
		return mock;
	}
}
