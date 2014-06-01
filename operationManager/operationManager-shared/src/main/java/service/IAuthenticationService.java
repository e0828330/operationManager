package service;

import model.User;

public interface IAuthenticationService {
	public User authenticate(String username, String password);
}
