package webGui;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

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

}
