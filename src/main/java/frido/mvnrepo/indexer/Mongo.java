package frido.mvnrepo.indexer;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

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

    public void update(String collection, Document query, Document newOne){
        UpdateOptions uo = new UpdateOptions();
        uo.upsert(true);
        UpdateResult result = db.getCollection(collection).replaceOne(query, newOne, uo);
        System.out.println(result.getMatchedCount() + " - " + result.getModifiedCount() + " - " + result.getUpsertedId());
    }
}