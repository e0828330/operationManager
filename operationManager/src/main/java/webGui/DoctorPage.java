package webGui;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;

@AuthorizeInstantiation("DOCTOR")
public class DoctorPage extends IndexPage {

	private static final long serialVersionUID = 2054052123886045788L;

	public DoctorPage() {
		super(IndexPage.ActivePage.DOCTORPAGE);
		add(new Label("bla", "Doktor-Area"));
	}

}
