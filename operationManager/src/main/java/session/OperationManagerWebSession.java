package session;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.springframework.beans.factory.annotation.Autowired;

import service.IAuthenticationService;

public class OperationManagerWebSession extends AuthenticatedWebSession {

	private static final long serialVersionUID = 8512888632456915860L;
	
	@Autowired
	IAuthenticationService authenticationService;
	
	private Role activeRole = Role.HOSPITAL;
	
	public Role getActiveRole() {
		return activeRole;
	}
	
	public enum Role {
		DEFAULT, DOCTOR, HOSPITAL, PATIENT
	}

	
	public OperationManagerWebSession(Request request) {
		super(request);
	}

	// TODO: authentication
	@Override
	public boolean authenticate(String username, String password) {
		activeRole = authenticationService.authenticate(username, password);
		
		return false;
	}

	@Override
	public Roles getRoles() {
		Roles roles = new Roles();
		roles.add(this.activeRole.name());
		return roles;
	}

}
