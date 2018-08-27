package frido.mvnrepo.indexer.core.download;

import org.bson.Document;

import frido.mvnrepo.indexer.core.db.Database;

public class MetadataProvider implements Provider {

	private Database db;

	public MetadataProvider(Database database) {
		this.db = database;
	}

	@Override
	public Iterable<Document> provide() {
		return db.getAll("metadata");
	}

}
