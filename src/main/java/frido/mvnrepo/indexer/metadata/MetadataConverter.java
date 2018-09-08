package frido.mvnrepo.indexer.metadata;

import org.bson.Document;

import frido.mvnrepo.indexer.core.db.Converter;
import frido.mvnrepo.indexer.data.Metadata;

public class MetadataConverter implements Converter<Metadata, Document> {

	@Override
	public Document toEntity(Metadata record) {
		return record.getDocument();
	}

	@Override
	public Metadata toRecord(Document doc) {
		return new Metadata(doc);
	}

}
