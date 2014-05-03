package webGui;

import java.util.Hashtable;
import model.Role;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebSession;

import session.OperationManagerWebSession;

import utils.TemplateConstants;

public class IndexPage extends WebPage {
	
	private static final long serialVersionUID = -4448644188854079942L;
	
	private String defaultTitle = "Startpage OperationsManager";
			
	private Hashtable<String, Class<? extends Page>> links;
	
	public IndexPage() {		
		final OperationManagerWebSession session = (OperationManagerWebSession) WebSession.get();

		// Role
		String role = session.getRoles().isEmpty() ? "" : session.getRoles().toString();

		// Login form
		final TextField<String> username = new TextField<String>("wusername", Model.of(""));
		final TextField<String> password = new PasswordTextField("wpassword", Model.of(""));
		Form<?> loginForm = new Form<Void>(TemplateConstants.LOGIN_FORM) {
			private static final long serialVersionUID = 1L;
			protected void onSubmit() {
				String username_value = username.getModelObject();
				String password_value = password.getModelObject();
				session.authenticate(username_value, password_value);
				setResponsePage(StartPage.class);
			};
		};
		loginForm.add(username);
		loginForm.add(password);
		
		// Logout form
		Form<?> logoutForm = new Form<Void>(TemplateConstants.LOGOUT_FORM) {
			private static final long serialVersionUID = 1L;
			protected void onSubmit() {
				session.logout();
				setResponsePage(StartPage.class);
			};
		};

		
		
		// If logged in, hide login formular
		if (session.getActiveUser() != null) {
			loginForm.setVisible(false);
			logoutForm.setVisible(true);
			logoutForm.add(new Label("wloginrole", role + "|" + session.getActiveUser().getUsername()));
		}
		else {
			loginForm.setVisible(true);
			logoutForm.setVisible(false);			
		}
		
		add(new Label(TemplateConstants.PAGE_TITLE, this.defaultTitle));
		add(loginForm);
		add(logoutForm);
	}
	
}
