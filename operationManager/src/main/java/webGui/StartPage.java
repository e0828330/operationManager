package webGui;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;

import webGui.overview.OverviewPanel;


public class StartPage extends IndexPage {
	private static final long serialVersionUID = -5751552070055708995L;
	
	public StartPage() {
		add(new OverviewPanel("overview"));

	}
	

}
