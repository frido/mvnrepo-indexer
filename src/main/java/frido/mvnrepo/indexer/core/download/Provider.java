package frido.mvnrepo.indexer.core.download;

import org.bson.Document;

public interface Provider {
	public Iterable<Document> provide();
}
