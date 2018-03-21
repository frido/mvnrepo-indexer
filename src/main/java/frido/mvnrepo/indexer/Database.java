package frido.mvnrepo.indexer;

import org.bson.Document;

interface Database {
    void save(String collection, Document doc);
    public Iterable<Document> getAll(String collection);
    public Iterable<Document> getGitHubRelated();
    public void update(String collection, Document query, Document newOne);
}