package webGui;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Test;

public class TestStartPage extends AbstractBaseTest {

	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(8080);
		 
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/test");
        webapp.setWar("src/main/webapp");
        webapp.setOverrideDescriptor("src/main/webapp/WEB-INF/web-test.xml");
        server.setHandler(webapp);
 
		server.start();
        server.join();
	}
	
	@Test
	public void should_render_table() {
		tester.startPage(StartPage.class);
		
		tester.assertRenderedPage(StartPage.class);
		
		tester.assertComponent("overview:overviewTable", DataTable.class);
	}
}
