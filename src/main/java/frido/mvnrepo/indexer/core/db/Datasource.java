package frido.mvnrepo.indexer.core.db;

import org.bson.Document;

public interface Datasource {
	public void save(String collection, Document record);

	public void update(String collection, Document record, Object identifier);

	public Iterable<Document> getAll(String collection);
}