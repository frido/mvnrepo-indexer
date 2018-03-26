package frido.mvnrepo.indexer;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mongo {

    Logger log = LoggerFactory.getLogger(Mongo.class);

    private MongoDatabase db;

    @SuppressWarnings("resource")
    public Mongo(String connectionString){
        MongoClientURI uri  = new MongoClientURI(connectionString);
        MongoClient client = new MongoClient(uri);
        db = client.getDatabase(uri.getDatabase());
    }

    public Document save(String collection, Document doc) {
        db.getCollection(collection).insertOne(doc);
        return doc;
    }

    public Iterable<Document> getAll(String collection) {
        return db.getCollection(collection).find();
    }

    public Iterable<Document> getByFilter(String collection, Document filter) {
        return db.getCollection(collection).find(filter);
    }

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
        // TODO: toto neviem spravit v jednom query
        //return db.getCollection("pom").aggregate(Arrays.asList(filter, Aggregates.group("$Url")));
    }

    public void update(String collection, Document query, Document newOne){
        log.trace("update: {}", newOne);
        UpdateOptions uo = new UpdateOptions();
        uo.upsert(true);
        db.getCollection(collection).replaceOne(query, newOne, uo);
    }
}