package webGui;

import org.apache.wicket.markup.html.WebPage;

import webGui.overview.OverviewPanel;

public class SimplePage extends WebPage {
	private static final long serialVersionUID = -5751552070055708995L;

	
	public SimplePage() {
		add(new OverviewPanel("overview"));
	}
}
