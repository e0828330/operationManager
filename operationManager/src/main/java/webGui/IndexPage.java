package webGui;

import model.Doctor;
import model.Hospital;
import model.Patient;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import session.OperationManagerWebSession;
import utils.TemplateConstants;

public class IndexPage extends WebPage {
	
	private static final long serialVersionUID = -4448644188854079942L;
	
	private String defaultTitle = "Startpage OperationsManager";
	
	public IndexPage(final PageParameters parameters) {		
		final OperationManagerWebSession session = (OperationManagerWebSession) WebSession.get();
		
		if (parameters != null && !parameters.isEmpty() &&  parameters.get("authenticated") != null) {	
			if (parameters.get("authenticated").toString().equals("success")) {
				add(new Label("authentication_failure").setVisible(false));
			}
			else {
				add(new Label("authentication_failure", "Login fehlgeschlagen!").setVisible(true));
			}
		}
		else if (session.getActiveUser() != null) {
			add(new Label("authentication_failure").setVisible(false));
		}
		else {
			add(new Label("authentication_failure").setVisible(false));
		}
		
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
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("authenticated", session.getActiveUser() == null ? "failure" : "success");
				setResponsePage(StartPage.class, pageParameters);
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
			if (session.getActiveUser() instanceof Patient) {
				Patient p = (Patient) session.getActiveUser();
				logoutForm.add(new Label("wloginrole", p.getFirstName() + " " + p.getLastName()));
			}
			else if (session.getActiveUser() instanceof Doctor) {
				Doctor d = (Doctor) session.getActiveUser();
				logoutForm.add(new Label("wloginrole", d.getFirstName() + " " + d.getLastName()));
			}
			else {
				Hospital h = (Hospital) session.getActiveUser();
				logoutForm.add(new Label("wloginrole", h.getName()));
			}
			
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
