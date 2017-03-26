package configuration;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.BasicDBObject;
//import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;


@Configuration
class SampleMongoConfiguration extends AbstractMongoConfiguration {
	
	@Override
	protected String getDatabaseName() {
		return MongoCredentials.database;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Mongo mongo() throws Exception {
		return new Mongo();
	}

	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		
		MongoClient mongoClient = null;
		ServerAddress serverAddress = new ServerAddress(MongoCredentials.host, MongoCredentials.port);
		
		
		if(MongoCredentials.user!=null){
			// Set credentials
			MongoCredential credential = MongoCredential.createCredential(MongoCredentials.user, getDatabaseName(), MongoCredentials.password.toCharArray());
	
			// Mongo Client
			mongoClient = new MongoClient(serverAddress, Arrays.asList(credential));
		}
		else
			mongoClient = new MongoClient(serverAddress);
		
		// Mongo DB Factory
		SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(mongoClient, getDatabaseName());
		
		simpleMongoDbFactory.getDb().getCollection(getDatabaseName()).createIndex(new BasicDBObject("id", -1));
		
		return simpleMongoDbFactory;

	}

	@SuppressWarnings("deprecation")
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		
		mongoTemplate.setWriteConcern(WriteConcern.SAFE);
		
		
		return mongoTemplate;
	}

}
