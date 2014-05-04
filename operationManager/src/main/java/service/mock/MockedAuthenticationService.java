package service.mock;

import model.Doctor;
import model.Hospital;
import model.Patient;
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

				if (username.equals("patient")) {
					Patient p = new Patient();
					p.setRole(Role.PATIENT);
					p.setUsername("pastient");
					p.setFirstName("Wayne");
					p.setLastName("TestingPatient");
					return p;
				}
				else if (username.equals("doctor")) {
					Doctor d = new Doctor();
					d.setRole(Role.DOCTOR);
					d.setUsername("doctor");
					d.setFirstName("Wayne");
					d.setLastName("TestingDoctor");
					return d;
				}
				else if (username.equals("hospital")) {
					Hospital h = new Hospital();
					h.setRole(Role.HOSPITAL);
					h.setUsername("hospital");
					h.setName("TestingHospital");
					return h;
				}
				return null;
			}
			
		});
		
		return mock;
	}
}
