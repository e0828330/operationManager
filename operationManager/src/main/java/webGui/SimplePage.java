package webGui;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.TestService;

public class SimplePage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5751552070055708995L;

	@SpringBean
	private TestService service;
	
	public SimplePage() {
		System.out.println(service.getString());
		add(new Label("msg", "test"));
	}
}
