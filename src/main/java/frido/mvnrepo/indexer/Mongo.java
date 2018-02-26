package frido.mvnrepo.indexer;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class Mongo {
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
}