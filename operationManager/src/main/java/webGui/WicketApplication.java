package webGui;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.core.util.resource.locator.ResourceStreamLocator;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.string.Strings;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import session.OperationManagerWebSession;
import webGui.notification.NotificationsPage;

@Configuration
@ComponentScan
public class WicketApplication extends AuthenticatedWebApplication {

	/**
	 * Copied from wicket examples
	 *
	 */
	private final class CustomResourceStreamLocator extends ResourceStreamLocator {
		/**
		 * @see org.apache.wicket.core.util.resource.locator.ResourceStreamLocator#locate(java.lang.Class,
		 *      java.lang.String)
		 */
		@Override
		public IResourceStream locate(Class<?> clazz, String path) {

			String location;
			
			// this custom case disregards the path and tries it's own
			// scheme
			String extension = path.substring(path.lastIndexOf('.') + 1);
			String simpleFileName = Strings.lastPathComponent(clazz.getName(), '.');
			location = "/templates/" + simpleFileName + "." + extension;

			URL url;
			try {
				// try to load the resource from the web context
				url = getServletContext().getResource(location);
				if (url != null) {
					return new UrlResourceStream(url);
				}
			} catch (MalformedURLException e) {
				throw new WicketRuntimeException(e);
			}

			// resource not found; fall back on class loading
			return super.locate(clazz, path);
		}

	}

	@Override
	public Class<StartPage> getHomePage() {
		return StartPage.class;
	}

	@Override
	protected void init() {
		super.init();
		this.getMarkupSettings().setStripWicketTags(true);
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		getResourceSettings().setResourceStreamLocator(new CustomResourceStreamLocator());
		
		//mount pages
		mountPage("/", StartPage.class);
		mountPage("/notifications", NotificationsPage.class);
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
