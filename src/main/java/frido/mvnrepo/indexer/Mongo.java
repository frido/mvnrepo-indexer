package frido.mvnrepo.indexer;


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

    // TODO: group by
    public Iterable<Document> getGitHubRelated(){
        Document filter = new Document();
        Document regex = new Document();
        regex.put("$regex", "^https://github.com/.+?/.+");
        regex.put("$options", "i");
        filter.put("projectUrl", regex);
		return db.getCollection("metadata").find(filter);
    }

    // TODO: test na toto
    public void update(String collection, Document query, Document newOne){
        log.trace("update: {}", newOne);
        UpdateOptions uo = new UpdateOptions();
        uo.upsert(true);
        db.getCollection(collection).replaceOne(query, newOne, uo);
    }
}