package service.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Notification;
import model.NotificationType;
import model.OperationStatus;
import model.OperationType;
import model.User;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import service.INotificationService;

@Service
public class MockedNotificationService {

	@Bean
	INotificationService getMockedNotificationService() {
		INotificationService mock = Mockito.mock(INotificationService.class);
		
		Mockito.when(mock.getForUser(Mockito.any(User.class))).thenAnswer(new Answer<List<Notification>>() {

			@Override
			public List<Notification> answer(InvocationOnMock invocation)
					throws Throwable {
				User user = (User) invocation.getArguments()[0];
				List<Notification> list = new ArrayList<Notification>();

				list.add(getMockedNotification(user, NotificationType.RESERVATION_SUCESSFULL));
				list.add(getMockedNotification(user, NotificationType.RESERVATION_FAILED));
				list.add(getMockedNotification(user, NotificationType.RESERVATION_DELETED));
				
				return list;
			}
			
		});
		
		return mock;
	}
	
	public Notification getMockedNotification(User user, NotificationType type) {
		Notification notification = new Notification();
		
		notification.setTimestamp(new Date());
		notification.setRecipient(user);
		notification.setSlot(MockedOPSlotService.getMockedOPSlot("1", OperationType.eye, "SMZ", "Dr. Augfehler", "Adelheid", "Abesser", OperationStatus.reserved));
		notification.setMessage("notification text");
		notification.setType(type);
		
		return notification;
	}
}
