package service;

import model.Role;

public interface IAuthenticationService {
	Role authenticate(String username, String password);
}
