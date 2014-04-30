package service.real;

import model.Role;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import service.IAuthenticationService;

@Service
public class AuthenticationService implements IAuthenticationService {

	@Bean
	static IAuthenticationService getAuthenticationService() {
		return new AuthenticationService();
	}	
	
	@Override
	public Role authenticate(String username, String password) {
		return Role.DOCTOR;
	}
	
}