package newsbeeper;

import model.Notification;
import model.dto.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import service.IQueueListener;
import service.IQueueService;
import config.RabbitMQConfig;

@Component
public class Main {

	
	@Autowired
	IQueueService queueService;
		
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("/spring/test-config.xml");

	}	
	
	private void registerListener() {
		// Register newsbeep async listener
		queueService.registerListener(RabbitMQConfig.NEWSBEEPER_Q, new IQueueListener() {
			
			@Override
			public void handleMessage(Message m) {
				Notification notification = (Notification) m;
				
			}
		});			
	}
}
