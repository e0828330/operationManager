package webGui;

import model.Doctor;
import model.Hospital;
import model.Patient;
import model.Role;
import model.User;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import session.OperationManagerWebSession;
import utils.TemplateConstants;

public class IndexPage extends WebPage {
	
	private static final long serialVersionUID = -4448644188854079942L;
	
	private String defaultTitle = "Startpage OperationsManager";
	
	public IndexPage(final PageParameters parameters) {		
		final OperationManagerWebSession session = (OperationManagerWebSession) WebSession.get();
		
		if (parameters != null && !parameters.isEmpty() && !parameters.get("authenticated").isEmpty()) {	
			if (parameters.get("authenticated").toString().equals("success")) {
				add(new Label("authentication_failure").setVisible(false));
			}
			else {
				add(new Label("authentication_failure", "Login fehlgeschlagen!").setVisible(true));
			}
		}
		else {
			add(new Label("authentication_failure").setVisible(false));
		}

		// Login form
		CompoundPropertyModel<User> loginModel = new CompoundPropertyModel<User>(new User());
		Form<User> loginForm = new Form<User>(TemplateConstants.LOGIN_FORM, loginModel) {
			private static final long serialVersionUID = 1L;
			protected void onSubmit() {
				User user = getModelObject();
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("authenticated", !session.authenticate(user.getUsername(), user.getPassword()) ? "failure" : "success");
				setResponsePage(StartPage.class, pageParameters);
			};
		};
		loginForm.add(new TextField<String>("username"));
		loginForm.add( new PasswordTextField("password"));
		
		// Logout form
		Form<?> logoutForm = new Form<Void>(TemplateConstants.LOGOUT_FORM) {
			private static final long serialVersionUID = 1L;
			protected void onSubmit() {
				session.logout();
				setResponsePage(StartPage.class);
			};
		};

		
		
		// If logged in, hide login formular
		if (!session.getActiveUser().getRole().equals(Role.DEFAULT)) {
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
			else if (session.getActiveUser() instanceof Hospital){
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
