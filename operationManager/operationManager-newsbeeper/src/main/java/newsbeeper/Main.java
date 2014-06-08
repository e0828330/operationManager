package newsbeeper;

import model.Notification;
import model.dto.Message;
import model.dto.NotificationDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import service.INewsbeeperService;
import service.INotificationService;
import service.IQueueListener;
import service.IQueueService;
import config.RabbitMQConfig;

@Component
public class Main implements InitializingBean {

	private Logger logger = LoggerFactory.getLogger(Main.class);

	@Autowired
	private IQueueService queueService;

	@Autowired
	private INewsbeeperService newsbeeperService;

	@Autowired
	private INotificationService notificationService;

	private static ApplicationContext context;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("/spring/application-config.xml");
		Main prog = new Main();
		AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
		factory.autowireBeanProperties(prog, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
	}

	private void listenOnQueue() {
		queueService.registerListener(RabbitMQConfig.NEWSBEEPER_Q, new IQueueListener() {
			@Override
			public void handleMessage(Message m) {
				
				Notification tmp = newsbeeperService.handleNotification((NotificationDTO) m);
				if (tmp != null) notificationService.save(tmp);
					
			}
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Started listening");
		listenOnQueue();
	}

}
