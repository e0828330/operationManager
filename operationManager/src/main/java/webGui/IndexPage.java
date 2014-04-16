package webGui;

import java.util.Hashtable;
import java.util.Map.Entry;

import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.protocol.http.WebSession;

import session.OperationManagerWebSession;
import session.OperationManagerWebSession.Role;
import utils.TemplateConstants;

public class IndexPage extends WebPage {
	
	private static final long serialVersionUID = -4448644188854079942L;
	
	private String defaultTitle = "Startpage OperationsManager";
	
	private Role activeRole = Role.DEFAULT;
			
	public enum ActivePage {
		STARTPAGE, PATIENTPAGE, DOCTORPAGE, HOSPITALPAGE
	}
	
	private Hashtable<String, Class<? extends Page>> links;
	
	public IndexPage(ActivePage active) {
		add(new Label(TemplateConstants.PAGE_TITLE, this.defaultTitle));
		
		this.links = new Hashtable<>();
		
		OperationManagerWebSession session = (OperationManagerWebSession) WebSession.get();
		
		links.put(ActivePage.STARTPAGE.name().toLowerCase(), StartPage.class);
		links.put(ActivePage.PATIENTPAGE.name().toLowerCase(), PatientPage.class);
		links.put(ActivePage.DOCTORPAGE.name().toLowerCase(), DoctorPage.class);
		links.put(ActivePage.HOSPITALPAGE.name().toLowerCase(), HospitalPage.class);
		
		// Retrieve roles from session and set the active role
		// We always have the role DEFAULT, if we find another one
		// we overwrite the DEFAULT role
		for (String role : session.getRoles()) {
			if (role.equals(Role.DOCTOR.name())) {
				activeRole = Role.DOCTOR;
				break;
			}
			else if (role.equals(Role.HOSPITAL.name())) {
				activeRole = Role.HOSPITAL;
				break;
			}
			else if (role.equals(Role.PATIENT.name())) {
				activeRole = Role.PATIENT;
				break;
			}
		}
		
		
		for (Entry<String, Class<? extends Page>> entry : this.links.entrySet()) {
			BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>(entry.getKey(), entry.getValue()); 
				link.setVisible(getLinkVisibility(activeRole, ActivePage.valueOf(entry.getKey().toUpperCase())));
				if (entry.getValue().getSimpleName().equals(this.getClass().getSimpleName())) {
					link.add(new AttributeAppender("class", "active_link"));
				}
		
			add(link);
		}
	} 
	
	/*
	 * Returns the visibility of the current link related to the role
	 */
	private boolean getLinkVisibility(Role role, ActivePage page) {
		if (page == ActivePage.STARTPAGE) {
			return true;
		}
		if (role == Role.DOCTOR && page == ActivePage.DOCTORPAGE) {
			return true;
		}
		if (role == Role.HOSPITAL && page == ActivePage.HOSPITALPAGE) {
			return true;
		}
		if (role == Role.PATIENT && page == ActivePage.PATIENTPAGE) {
			return true;
		}
		return false;
	}
	
}
