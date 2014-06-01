package session;

import model.Role;
import model.User;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.IAuthenticationService;

public class OperationManagerWebSession extends AuthenticatedWebSession {

	private static final long serialVersionUID = 8512888632456915860L;
	
	@SpringBean(name="getAuthenticationService")
	IAuthenticationService authenticationService;
	
	// Data for login
	private Role activeRole = Role.DEFAULT;
	private User activeUser = null;
	
	// For authentication
	private Roles roles = new Roles();
	
	/**
	 * Returns the active roles for authentication.
	 * If no user is logged in, it will store DEFAULT.
	 * Otherwise it will consist one of the values: DOCTOR, HOSPITAL or PATIENT of Enum Role.
	 * @return The current authenticated Role.
	 */
	public Role getActiveRole() {
		return this.activeRole;
	}
	
	/**
	 * Returns the active user for authentication.
	 * If no user is logged in, it will store null.
	 * @return The current authenticated User.
	 */	
	public User getActiveUser() {
		if (this.activeUser == null) {
			this.activeUser = new User();
			this.activeUser.setRole(Role.DEFAULT);
		}
		return this.activeUser;
	}
	
	public OperationManagerWebSession(Request request) {
		super(request);
		Injector.get().inject(this);
	}

	@Override
	public boolean authenticate(String username, String password) {
		// reset stored login
		this.roles.clear();
		this.activeRole = Role.DEFAULT;
		this.activeUser = null;
		
		// check login
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
		this.activeUser = null;
		this.activeRole = Role.DEFAULT;
	}
	
	private void checkLogin(String username, String password) {
		this.activeUser =  authenticationService.authenticate(username, password);
		this.activeRole = this.activeUser.getRole();
	}

}
