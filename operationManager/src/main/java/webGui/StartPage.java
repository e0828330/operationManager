package webGui;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import webGui.overview.OverviewPanel;


public class StartPage extends IndexPage {
	private static final long serialVersionUID = -5751552070055708995L;
	
	public StartPage(final PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new OverviewPanel("overview"));
	}

}
