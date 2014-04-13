package webGui;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.management.Attribute;

import org.apache.wicket.Page;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import utils.TemplateConstants;

public class IndexPage extends WebPage {
	
	private static final long serialVersionUID = -4448644188854079942L;
	
	private String defaultTitle = "Startpage OperationsManager";
		
	// Constants for links
	public static final String LINK_START = "startpage";
	public static final String LINK_LOGINPAGE = "loginpage";

	protected String active = IndexPage.LINK_START;
	
	private Hashtable<String, Class<? extends Page>> links;
	
	public IndexPage() {
		add(new Label(TemplateConstants.PAGE_TITLE, this.defaultTitle));
		
		this.links = new Hashtable<>();
		links.put(IndexPage.LINK_START, StartPage.class);
		links.put(IndexPage.LINK_LOGINPAGE, LoginPage.class);
		
		for (Entry<String, Class<? extends Page>> entry : this.links.entrySet()) {
			BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>(entry.getKey(), entry.getValue()); 
				if (entry.getValue().getSimpleName().equals(this.getClass().getSimpleName())) {
					link.add(new AttributeAppender("class", "active_link"));
				}
		
			add(link);
		}

	} 
	
}
