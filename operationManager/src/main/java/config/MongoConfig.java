package config;

import org.bson.BSON;
import org.bson.Transformer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories
public class MongoConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "operations-db";
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
		
		return new MongoClient();
	}

}
