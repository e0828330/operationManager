package georesolver;

import model.dto.Message;
import model.dto.OPSlotDTO;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import service.IOPSlotService;
import service.IQueueListener;
import service.IQueueService;
import config.RabbitMQConfig;

@Component
public class Main implements InitializingBean {

	
	@Autowired
	IQueueService queueService;
	
	@Autowired
	IOPSlotService opSlotService;

	private static ApplicationContext context;

	@Override
	public void afterPropertiesSet() throws Exception {		
	}
	
	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("/spring/test-config.xml");
		Main prog = new Main();
		AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
		factory.autowireBeanProperties(prog, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
		factory.initializeBean(prog, "georesolver");
		prog.run();
	}
	
	private void registerListener() {
		// Register georesolver async listener
		queueService.registerListener(RabbitMQConfig.GEORESOLVER_Q, new IQueueListener() {
			@Override
			public void handleMessage(Message m) {
				OPSlotDTO slot = (OPSlotDTO) m;
				System.out.println(slot);
			}
		});		
	}

	
	void run() {
		registerListener();
	}
}
