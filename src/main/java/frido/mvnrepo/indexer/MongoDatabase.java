package frido.mvnrepo.indexer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.model.UpdateOptions;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MongoDatabase implements Database {

    Logger log = LoggerFactory.getLogger(MongoDatabase.class);
    private static final String ENV_MONGO_URL = "MONGO_URL";
    private com.mongodb.client.MongoDatabase db;

    @SuppressWarnings("resource")
    public MongoDatabase() {
        String connectionString = System.getenv().get(ENV_MONGO_URL);
        MongoClientURI uri  = new MongoClientURI(connectionString);
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
    public Iterable<Document> getByFilter(String collection, Document filter){
        return db.getCollection(collection).find(filter);
    }

    @Override
    public Collection<Document> getGitHubRelated(){
        Document filter = new Document();
        Document regex = new Document();
        regex.put("$regex", "^https://github.com/.+?/.+");
        regex.put("$options", "i");
        filter.put("Url", regex);
        Map<String, Document> hash = new HashMap<>();
        db.getCollection("pom").find(filter).forEach(new Consumer<Document>() {

			@Override
			public void accept(Document doc) {
				hash.put(doc.getString("Url"), doc);
			}
        });
        return hash.values();
    }

    @Override
    public void update(String collection, Document query, Document newOne){
        log.trace("update: {}", newOne);
        UpdateOptions uo = new UpdateOptions();
        uo.upsert(true);
        db.getCollection(collection).replaceOne(query, newOne, uo);
    }

}