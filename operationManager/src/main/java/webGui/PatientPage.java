package webGui;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;

@AuthorizeInstantiation("PATIENT")
public class PatientPage extends IndexPage {
	private static final long serialVersionUID = -5751552070055708995L;

	public PatientPage() {
		super(IndexPage.ActivePage.PATIENTPAGE);
		add(new Label("bla", "Patient-Area"));
	
	}
}
