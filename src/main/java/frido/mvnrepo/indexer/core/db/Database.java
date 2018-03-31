package frido.mvnrepo.indexer.core.db;

import org.bson.Document;

public interface Database {
    public Document save(String collection, Document doc);
    public Iterable<Document> getAll(String collection);
    public Iterable<Document> getByFilter(String collection, Document filter);
    public Iterable<Document> getGitHubRelated();
    public void update(String collection, Document query, Document newOne);
}