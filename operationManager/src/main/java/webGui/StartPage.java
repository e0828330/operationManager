package webGui;

import org.apache.wicket.markup.html.basic.Label;

public class StartPage extends IndexPage {
	private static final long serialVersionUID = -5751552070055708995L;
	
	public StartPage() {
		active = IndexPage.LINK_LOGINPAGE;
		add(new Label("bla", "start"));

	}
}
