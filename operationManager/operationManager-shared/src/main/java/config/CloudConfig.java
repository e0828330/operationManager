package config;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.config.java.ServiceScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;

//@Configuration
//@ServiceScan
public class CloudConfig extends AbstractCloudConfig {
	/* @Bean disabled during local development */
	public MongoDbFactory cloudMongoDbFactory() {
		return connectionFactory().mongoDbFactory("DB");
	}
}
