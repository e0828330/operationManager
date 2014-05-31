package webGui;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import session.OperationManagerWebSession;
import webGui.notification.NotificationsPage;

@Configuration
@ComponentScan
public class WicketApplication extends AuthenticatedWebApplication {

	@Override
	public Class<StartPage> getHomePage() {
		return StartPage.class;
	}

	@Override
	protected void init() {
		super.init();
		this.getMarkupSettings().setStripWicketTags(true);
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		
		//mount pages
		//mountPage("/", StartPage.class);
		mountPage("/notifications", NotificationsPage.class);
		mountPage("/create", CreateOPSlotPage.class);
		mountPage("/reservation", ReservationPage.class);
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return StartPage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return OperationManagerWebSession.class;
	}

}
