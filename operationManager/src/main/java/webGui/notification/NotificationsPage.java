package webGui.notification;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import model.Notification;
import model.NotificationType;
import model.Role;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.INotificationService;
import session.OperationManagerWebSession;
import webGui.IndexPage;
import webGui.StartPage;
@AuthorizeInstantiation(value = {"HOSPITAL", "DOCTOR", "PATIENT"})
public class NotificationsPage extends IndexPage {
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	INotificationService notificationService;

	public NotificationsPage(PageParameters parameters) {
		super(parameters);
		
		OperationManagerWebSession session = (OperationManagerWebSession) WebSession.get();
		
		//Only registered users may view the notifications page
		/*if (session.getActiveUser().getRole().equals(Role.DEFAULT)) {
			throw new RestartResponseException(StartPage.class);
		}*/
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		IModel<List<Notification>> notificationsModel = new LoadableDetachableModel<List<Notification>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Notification> load() {
				OperationManagerWebSession session = (OperationManagerWebSession) WebSession.get();
				return notificationService.getForUser(session.getActiveUser());
			}
			
		};
		
		add(new WebMarkupContainer("emptyListContainer").setVisible(notificationsModel.getObject().isEmpty()));
		

		
		add(new ListView<Notification>("notificationList", notificationsModel) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected IModel<Notification> getListItemModel(
					IModel<? extends List<Notification>> listViewModel,
					int index) {
				return new CompoundPropertyModel<Notification>(super.getListItemModel(listViewModel, index));
			}
			
			@Override
			protected void populateItem(ListItem<Notification> item) {
				item.add(new DateLabel("timestamp", new PatternDateConverter("dd.MM.YY HH:mm:ss", true)));
				item.add(new Label("message"));
				item.add((new Label("type", new ResourceModel(item.getModelObject().getType().name()))).setEscapeModelStrings(false));
	
				
			}
		});
	}
		
}

