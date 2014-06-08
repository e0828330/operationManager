package rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import testData.DatabaseTest;

public abstract class TestServer extends DatabaseTest {
	
	protected static Server server;
	
	@BeforeClass
	public static void startServer() throws Exception {
		server = new Server(8080);
		 
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/operationManager-web");
        webapp.setWar("src/main/webapp");
        server.setHandler(webapp);
 
		server.start();
	}
	
	@AfterClass
	public static void stopServer() throws Exception {
		server.stop();
	}
}
