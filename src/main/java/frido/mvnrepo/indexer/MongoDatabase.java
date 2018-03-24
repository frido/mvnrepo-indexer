package frido.mvnrepo.indexer;

import org.bson.Document;

class MongoDatabase implements Database {

    private static final String ENV_MONGO_URL = "MONGO_URL";
    private Mongo mongo;

    // Merge with Mongo class
    public MongoDatabase() {
        String connectionString = System.getenv().get(ENV_MONGO_URL);
        this.mongo = new Mongo(connectionString);
    }

    @Override
    public void save(String collection, Document doc) {
        this.mongo.save(collection, doc);
    }

    @Override
    public Iterable<Document> getAll(String collection) {
        return this.mongo.getAll(collection);
    }

    @Override
    public Iterable<Document> getGitHubRelated(){
        return this.mongo.getGitHubRelated();
    }

    @Override
    public void update(String collection, Document query, Document newOne){
        this.mongo.update(collection, query, newOne);
    }

}