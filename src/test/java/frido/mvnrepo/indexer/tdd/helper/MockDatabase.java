package frido.mvnrepo.indexer.tdd.helper;

import org.bson.Document;

import frido.mvnrepo.indexer.core.db.Database;

public class MockDatabase implements Database {

	@Override
	public Document save(String collection, Document doc) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Document> getAll(String collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Document> getByFilter(String collection, Document filter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Document> findByUrlWithGithub() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(String collection, Document query, Document newOne) {
		throw new UnsupportedOperationException();
	}

}
