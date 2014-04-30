package session;

import model.Role;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.IAuthenticationService;

public class OperationManagerWebSession extends AuthenticatedWebSession {

	private static final long serialVersionUID = 8512888632456915860L;
	
	@SpringBean
	IAuthenticationService authenticationService;
	
	// Data for login
	private Role activeRole = Role.DEFAULT;
	//private String userID = null;
	
	
	
	
	// For authentication
	private Roles roles = new Roles();
	
	/**
	 * Returns the active roles for authentication.
	 * If no user is logged in, it will store DEFAULT.
	 * Otherwise it will consist one of the values: DOCTOR, HOSPITAL or PATIENT of Enum Role.
	 * @return The current authenticated Role.
	 */
	public Role getActiveRole() {
		return activeRole;
	}

	/**
	 * Returns the ID of the logged in user.
	 * @return The user-ID (hash value)
	 */
	/*public String getUserID() {
		return this.userID;
	}*/

	
	public OperationManagerWebSession(Request request) {
		super(request);
		Injector.get().inject(this);
	}

	@Override
	public boolean authenticate(String username, String password) {
		// reset stored login
		this.roles.clear();
		this.activeRole = Role.DEFAULT;
		//this.userID = null;
		
		// check login
		//System.out.println("try to login user: " + username + ", " + password);
		this.checkLogin(username, password);
		
		// store role
		this.roles.add(this.activeRole.name());

		return this.activeRole != Role.DEFAULT;
	}

	@Override
	public Roles getRoles() {

		if (this.roles.isEmpty()) {
			roles.add(this.activeRole.name());
		}
		return roles;
	}
	
	public void logout() {
		this.roles.clear();
		this.activeRole = Role.DEFAULT;
	}
	
	private void checkLogin(String username, String password) {
		this.activeRole =  authenticationService.authenticate(username, password);		
	}

}
