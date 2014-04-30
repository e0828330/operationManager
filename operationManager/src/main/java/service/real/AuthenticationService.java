package service.real;

import model.Role;

import org.springframework.stereotype.Service;

import service.IAuthenticationService;

@Service
public class AuthenticationService implements IAuthenticationService {

	@Override
	public Role authenticate(String username, String password) {
		
		return Role.DOCTOR;
	}
	
}