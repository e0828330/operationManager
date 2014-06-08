package service.mock;

import java.util.concurrent.ArrayBlockingQueue;

import model.dto.Message;
import model.dto.NotificationDTO;
import model.dto.OPSlotDTO;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import service.IQueueListener;
import service.IQueueService;
import config.RabbitMQConfig;

@Service
public class MockedQueueService {

	@Bean
	static IQueueService getQueueService() {
		
		final ArrayBlockingQueue<Message> geoResolverQueue = new ArrayBlockingQueue<>(10);
		final ArrayBlockingQueue<Message> newsBeeperQueue = new ArrayBlockingQueue<>(10);
		
		IQueueService mock =  Mockito.mock(IQueueService.class);
		
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				OPSlotDTO dto = (OPSlotDTO)invocation.getArguments()[0];
				geoResolverQueue.add(dto);
				return null;
			}
		}).when(mock).sendToGeoResolver(Mockito.any(OPSlotDTO.class));
		
		
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				NotificationDTO dto = (NotificationDTO)invocation.getArguments()[0];
				newsBeeperQueue.add(dto);
				return null;
			}
		}).when(mock).sendToNewsBeeper(Mockito.any(NotificationDTO.class));
	
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				final String queue = (String)invocation.getArguments()[0];
				final IQueueListener listener = (IQueueListener)invocation.getArguments()[1];
				new Thread(new Runnable() {
					@Override
					public void run() {
						if (queue.equals(RabbitMQConfig.GEORESOLVER_Q)) {
							try {
								listener.handleMessage(geoResolverQueue.take());
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						else if (queue.equals(RabbitMQConfig.NEWSBEEPER_Q)) {
							try {
								listener.handleMessage(newsBeeperQueue.take());
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
				return null;
			}
			
		}).when(mock).registerListener(Mockito.anyString(), Mockito.any(IQueueListener.class));
		
		return mock;
	}


}
