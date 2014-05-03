package georesolver;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import service.IOPSlotService;
import service.IQueueService;

@Component
public class Main implements InitializingBean {

	
	@Autowired
	IQueueService queueService;
	
	@Autowired
	IOPSlotService opSlotService;

	@Override
	public void afterPropertiesSet() throws Exception {		
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("/spring/test-config.xml");
		Main prog = new Main();
		AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
		factory.autowireBeanProperties(prog, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
		factory.initializeBean(prog, "georesolver");
		prog.run();
		((ConfigurableApplicationContext)context).close();
	}

	
	void run() {
		System.out.println(queueService); // Just a test
		System.out.println(opSlotService); // Just a test
	}
}
