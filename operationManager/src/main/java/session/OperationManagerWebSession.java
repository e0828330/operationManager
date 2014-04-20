package session;

import java.util.Hashtable;
import java.util.Map.Entry;

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
	
	private Hashtable<Role, Boolean> authenticated;

	
	public OperationManagerWebSession(Request request) {
		super(request);
	}

	// TODO: authentication
	@Override
	public boolean authenticate(String username, String password) {
		activeRole = authenticationService.authenticate(username, password);
		
		return activeRole != Role.DEFAULT;
	}

	@Override
	public Roles getRoles() {
		// Default
		this.authenticated = new Hashtable<>();
		this.authenticated.put(OperationManagerWebSession.Role.DEFAULT, true);
		this.authenticated.put(OperationManagerWebSession.Role.DOCTOR, false);
		this.authenticated.put(OperationManagerWebSession.Role.HOSPITAL, false);
		this.authenticated.put(OperationManagerWebSession.Role.PATIENT, false);
		
		Roles roles = new Roles();
		for (Entry<Role, Boolean> entry : this.authenticated.entrySet()) {
			if (entry.getValue()) {
				roles.add(entry.getKey().name());
			}
		}
		return roles;
	}

}
