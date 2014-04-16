package webGui;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;

@AuthorizeInstantiation("HOSPITAL")
public class HospitalPage extends IndexPage {

	private static final long serialVersionUID = 2054052123886045788L;

	public HospitalPage() {
		super(IndexPage.ActivePage.HOSPITALPAGE);
		add(new Label("bla", "Hospital-Area"));
	}

}
