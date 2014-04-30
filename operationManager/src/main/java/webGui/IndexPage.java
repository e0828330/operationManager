package webGui;

import java.util.Hashtable;

import model.Role;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

import utils.TemplateConstants;

public class IndexPage extends WebPage {
	
	private static final long serialVersionUID = -4448644188854079942L;
	
	private String defaultTitle = "Startpage OperationsManager";
	
	private Role activeRole = Role.DEFAULT;
			
	private Hashtable<String, Class<? extends Page>> links;
	
	public IndexPage() {
		add(new Label(TemplateConstants.PAGE_TITLE, this.defaultTitle));
	}
	
}
