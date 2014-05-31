package webGui;

import org.apache.wicket.Session;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;

import session.OperationManagerWebSession;

@ContextConfiguration(locations = {"classpath:spring/application-config.xml"})
public class AbstractBaseTest {
	
	protected WicketTester tester;
	
	@Before
	public void init() {
		tester = new WicketTester(new WicketApplication() {
			
			@Override
			public Session newSession(Request request, Response response) {
				return new OperationManagerWebSession(request);
			}
		});
	}
}
