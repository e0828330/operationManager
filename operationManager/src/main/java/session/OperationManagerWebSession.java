package session;

import java.util.Hashtable;
import java.util.Map.Entry;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

public class OperationManagerWebSession extends AuthenticatedWebSession {

	private static final long serialVersionUID = 8512888632456915860L;
	
	private String username = "";
	private String password = "";
	
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
		boolean success = username.trim().equals("doctor") && password.trim().equals("doctor");
		
		if (success) {
			this.username = username.trim();
			this.password = password.trim();
			this.authenticated.put(Role.DOCTOR, true);
		}
		return success;
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
