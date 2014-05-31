package webGui;

import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/test-config.xml"})
public class AbstractBaseTest {
	
	protected WicketTester tester;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Before
	public void init() {
		tester = new WicketTester(new WicketApplication() {
			@Override
			protected void setUpInjector() {
				 this.getComponentInstantiationListeners().add(
						 new SpringComponentInjector(this,
						 applicationContext));
			}
		});
	}
}
