package frido.mvnrepo.indexer.core;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import frido.mvnrepo.indexer.core.db.Database;

public class MockDatabase implements Database {

    private List<Document> updated = new ArrayList<>();;

	@Override
	public Document save(String collection, Document doc) {
		return null;
	}

	@Override
	public Iterable<Document> getAll(String collection) {
		return null;
	}

	@Override
	public Iterable<Document> getByFilter(String collection, Document filter) {
		return null;
	}

	@Override
	public Iterable<Document> getGitHubRelated() {
		return null;
	}

	@Override
	public void update(String collection, Document query, Document newOne) {
		updated.add(newOne);
    }
    
    public List<Document> getUpdated(){
        return updated;
    }

}