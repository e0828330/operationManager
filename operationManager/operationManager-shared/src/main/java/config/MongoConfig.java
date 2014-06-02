package config;

import org.bson.BSON;
import org.bson.Transformer;
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.MongoServiceInfo;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;

@Configuration
@EnableMongoRepositories
public class MongoConfig {


    @Bean
    public MongoDbFactory mongoDbFactory() {
    	// Workaround for enum bug
		// See: https://jira.mongodb.org/browse/JAVA-268
		// And: https://jira.spring.io/browse/DATAMONGO-627
		BSON.addEncodingHook(Enum.class, new Transformer() {
			@Override
			public Object transform(Object o) {
				return o.toString();
			}
		});
        CloudEnvironment cloudEnvironment = new CloudEnvironment();
        MongoServiceInfo serviceInfo = cloudEnvironment.getServiceInfo("DB", MongoServiceInfo.class);
        MongoServiceCreator serviceCreator = new MongoServiceCreator();
        return serviceCreator.createService(serviceInfo);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }
	
	/*
	@Autowired
	private MongoDbFactory mongoFactory;
	
	@Override
	protected String getDatabaseName() {
		return mongoFactory.getDb().getName();
	}
	
	@Override
	public Mongo mongo() throws Exception {


		// Workaround for enum bug
		// See: https://jira.mongodb.org/browse/JAVA-268
		// And: https://jira.spring.io/browse/DATAMONGO-627
		BSON.addEncodingHook(Enum.class, new Transformer() {
			@Override
			public Object transform(Object o) {
				return o.toString();
			}
		});

		
		return mongoFactory.getDb().getMongo();
	}*/

}
