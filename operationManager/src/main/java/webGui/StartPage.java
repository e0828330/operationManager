package webGui;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;

@AuthorizeInstantiation("DEFAULT")
public class StartPage extends IndexPage {
	private static final long serialVersionUID = -5751552070055708995L;
	
	public StartPage() {
		super(IndexPage.ActivePage.STARTPAGE);
		add(new Label("bla", "Start-Area"));

	}
	

}
