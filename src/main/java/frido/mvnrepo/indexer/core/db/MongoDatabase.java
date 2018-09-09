package frido.mvnrepo.indexer.core.db;

import java.io.IOException;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.model.UpdateOptions;

public class MongoDatabase implements Datasource {

	Logger log = LoggerFactory.getLogger(MongoDatabase.class);
	private static final String ENV_MONGO_URL = "MONGO_URL";
	private com.mongodb.client.MongoDatabase db;
	private String collection;

	@SuppressWarnings("resource")
	public MongoDatabase() {
		String connectionString = System.getenv().get(ENV_MONGO_URL);
		MongoClientURI uri = new MongoClientURI(connectionString);
		MongoClient client = new MongoClient(uri);
		db = client.getDatabase(uri.getDatabase());
	}

	@Override
	public void save(String collection, Document record) {
		db.getCollection(collection).insertOne(record);

	}

	@Override
	public void update(String collection, Document record, Object identifier) {
		Document query = new Document("Identifier", identifier);
		UpdateOptions uo = new UpdateOptions();
		uo.upsert(true);
		db.getCollection(collection).replaceOne(query, record, uo);
	}

	@Override
	public Iterable<Document> getAll(String collection) {
		return db.getCollection(collection).find();
	}

	@Override
	public void close() throws IOException {

		// TODO: close
	}
}