package service.mock;

import java.util.ArrayList;
import java.util.List;

import model.Patient;
import model.Role;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import service.IPatientService;

@Service
public class MockedPatientService {
	@Bean
	static IPatientService getPatientServicee() {
		IPatientService mock =  Mockito.mock(IPatientService.class);
		
		List<Patient> patients = new ArrayList<>();
		
		Patient p = new Patient();
		p.setId("1");
		p.setFirstName("Adelheid");
		p.setLastName("Abesser");
		p.setUsername("abesser");
		p.setRole(Role.PATIENT);
		patients.add(p);

		p = new Patient();
		p.setId("2");
		p.setFirstName("Peter");
		p.setLastName("Berger");
		p.setUsername("berger");
		p.setRole(Role.PATIENT);
		patients.add(p);
		
		p = new Patient();
		p.setId("3");
		p.setFirstName("Beatrix");
		p.setLastName("Bauer");
		p.setUsername("bauer");
		p.setRole(Role.PATIENT);
		patients.add(p);

		p = new Patient();
		p.setId("4");
		p.setFirstName("Franz");
		p.setLastName("Fiedler");
		p.setUsername("franz");
		patients.add(p);
		
		p = new Patient();
		p.setId("5");
		p.setFirstName("Gloria");
		p.setLastName("Geraus");
		p.setUsername("geraus");
		p.setRole(Role.PATIENT);
		patients.add(p);
		
		p = new Patient();
		p.setId("6");
		p.setFirstName("Manfred");
		p.setLastName("Takacs");
		p.setUsername("takacs");
		p.setRole(Role.PATIENT);
		patients.add(p);
		
		Mockito.when(mock.getPatients()).thenReturn(patients);

		final List<Patient> inputPatients = patients;
		Mockito.when(mock.getPatients(Mockito.anyString())).thenAnswer(new Answer<List<Patient>>() {
			@Override
			public List<Patient> answer(InvocationOnMock invocation) throws Throwable {
				String keyword = (String)invocation.getArguments()[0];

				keyword = keyword.toLowerCase();
				List<Patient> result = new ArrayList<>();
				for (Patient p : inputPatients) {
					if (p.getFirstName().toLowerCase().contains(keyword) || p.getLastName().toLowerCase().contains(keyword)) {
						result.add(p);
					}
				}
				return result;
			}
		});
		Mockito.when(mock.getById(Mockito.anyString())).thenAnswer(new Answer<Patient>() {
			@Override
			public Patient answer(InvocationOnMock invocation) throws Throwable {
				String id = (String)invocation.getArguments()[0];
				for (Patient p : inputPatients) {
					if (p.getId().equals(id)) {
						return p;
					}
				}
				return null;
			}
		});
		return mock;
	}
}
