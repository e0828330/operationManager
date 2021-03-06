package webGui.notification;

import java.util.List;

import model.Notification;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.INotificationService;
import session.OperationManagerWebSession;
import webGui.IndexPage;

@AuthorizeInstantiation(value = {"HOSPITAL", "DOCTOR", "PATIENT"})
public class NotificationsPage extends IndexPage {
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	INotificationService notificationService;

	public NotificationsPage(PageParameters parameters) {
		super(parameters);
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
		
		MarkupContainer container = new WebMarkupContainer("emptyListContainer");
		
		add(container.setVisible(notificationsModel.getObject().isEmpty()));
		
		add(new WebMarkupContainer("notificationListHeader").setVisible(!notificationsModel.getObject().isEmpty()));
		
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

