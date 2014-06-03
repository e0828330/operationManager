package cloudConfig;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.RabbitServiceInfo;
import org.cloudfoundry.runtime.service.messaging.RabbitServiceCreator;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.config.java.ServiceScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServiceScan
public class RabbitMQConfig {

	public static final String GEORESOLVER_Q = "geoResolverQueue";
	public static final String NEWSBEEPER_Q = "newsBeeperQueue";

	@Bean
    public ConnectionFactory connectionFactory() {
        CloudEnvironment cloudEnvironment = new CloudEnvironment();
        RabbitServiceInfo serviceInfo = cloudEnvironment.getServiceInfo("MoM", RabbitServiceInfo.class);
        RabbitServiceCreator serviceCreator = new RabbitServiceCreator();
        return serviceCreator.createService(serviceInfo);
    }
	

	@Bean
	public AmqpAdmin amqpAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	public RabbitTemplate rabbitTemplate() { 
		return new RabbitTemplate(connectionFactory());
	}

	@Bean
	public Queue geoResolverQueue() {
		return new Queue(GEORESOLVER_Q, true);
	}

	@Bean
	public Queue newsBeeperQueue() {
		return new Queue(NEWSBEEPER_Q, true);
	}
	
}
