package frido.mvnrepo.indexer.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import frido.mvnrepo.indexer.core.db.Database;

public class MockDatabase implements Database {

    private List<Document> updated = new ArrayList<>();

	@Override
	public Document save(String collection, Document doc) {
		return null;
	}

	@Override
	public Iterable<Document> getAll(String collection) {
        if("metadata".equals(collection)){
            Document doc = new Document()
                .append("groupId", "org.apache.abdera")
                .append("artifactId", "abdera")
                .append("version", "1.1.3");
            return Arrays.asList(doc);
        }
		return null;
	}

	@Override
	public Iterable<Document> getByFilter(String collection, Document filter) {
		return null;
	}

	@Override
	public Iterable<Document> getGitHubRelated() {
		return Arrays.asList(new Document("Url", "https://github.com/frido/mvnrepo-indexer"));
	}

	@Override
	synchronized public void update(String collection, Document query, Document newOne) {
		updated.add(newOne);
    }
    
    public List<Document> getUpdated(){
        return updated;
    }

}