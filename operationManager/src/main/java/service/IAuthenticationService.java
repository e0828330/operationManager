package service;

import session.OperationManagerWebSession.Role;

public interface IAuthenticationService {
	Role authenticate(String username, String password);
}
