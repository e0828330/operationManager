package webGui;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.util.tester.WicketTester;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Test;

public class TestSimplePage {

	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(8080);
		 
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/test");
        webapp.setWar("src/main/webapp");
        server.setHandler(webapp);
 
		server.start();
        server.join();
	}
	
	@Test
	public void should_render_table() {
		WicketTester tester = new WicketTester();
		
		tester.startPage(SimplePage.class);
		
		tester.assertRenderedPage(SimplePage.class);
		
		tester.assertComponent("overviewTable", DataTable.class);
	}

}
