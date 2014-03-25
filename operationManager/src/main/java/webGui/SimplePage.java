package webGui;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.TestService;
import webGui.overview.OverviewPanel;

public class SimplePage extends WebPage {
	private static final long serialVersionUID = -5751552070055708995L;

	
	public SimplePage() {
		add(new OverviewPanel("overview"));
	}
}
