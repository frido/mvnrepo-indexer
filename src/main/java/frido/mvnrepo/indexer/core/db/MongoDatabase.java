package frido.mvnrepo.indexer.core.db;

import java.util.Arrays;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

public class MongoDatabase implements Database {

	Logger log = LoggerFactory.getLogger(MongoDatabase.class);
	private static final String ENV_MONGO_URL = "MONGO_URL";
	private com.mongodb.client.MongoDatabase db;

	@SuppressWarnings("resource")
	public MongoDatabase() {
		String connectionString = System.getenv().get(ENV_MONGO_URL);
		MongoClientURI uri = new MongoClientURI(connectionString);
		MongoClient client = new MongoClient(uri);
		db = client.getDatabase(uri.getDatabase());
	}

	@Override
	public Document save(String collection, Document doc) {
		db.getCollection(collection).insertOne(doc);
		return doc;
	}

	@Override
	public Iterable<Document> getAll(String collection) {
		return db.getCollection(collection).find();
	}

	@Override
	public Iterable<Document> getByFilter(String collection, Document filter) {
		return db.getCollection(collection).find(filter);
	}

	@Override
	public Iterable<Document> findByUrlWithGithub() {
		return db.getCollection("pom")
				.aggregate(Arrays.asList(Aggregates.match(Filters.regex("Url", "^https://github.com/.+?/.+")),
						Aggregates.group("$Url", Accumulators.first("Url", "$Url"))));
	}

	@Override
	public void update(String collection, Document query, Document newOne) {
		log.trace("update: {}", newOne);
		UpdateOptions uo = new UpdateOptions();
		uo.upsert(true);
		db.getCollection(collection).replaceOne(query, newOne, uo);
	}

}