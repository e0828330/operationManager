package webGui;

import java.util.Hashtable;
import java.util.Map.Entry;

import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.protocol.http.WebSession;

import session.OperationManagerWebSession;
import session.OperationManagerWebSession.Role;
import utils.TemplateConstants;

public class IndexPage extends WebPage {
	
	private static final long serialVersionUID = -4448644188854079942L;
	
	private String defaultTitle = "Startpage OperationsManager";
			
	private Hashtable<String, Class<? extends Page>> links;
	
	public IndexPage() {		
		OperationManagerWebSession session = (OperationManagerWebSession) WebSession.get();

		// Role
		String role = session.getRoles().isEmpty() ? "" : session.getRoles().toString();
		
		add(new Label(TemplateConstants.PAGE_TITLE, this.defaultTitle));
		add(new Label("loginrole", role));
	}
	
}
