package frido.mvnrepo.indexer.metadata;

import org.bson.Document;

import frido.mvnrepo.indexer.core.db.AbstractRepository;
import frido.mvnrepo.indexer.core.db.Converter;
import frido.mvnrepo.indexer.core.db.Datasource;
import frido.mvnrepo.indexer.data.Metadata;

public class MetadataRepository extends AbstractRepository<Metadata> {

	public MetadataRepository(Datasource db, Converter<Metadata, Document> converter) {
		super(db, converter);
	}

	@Override
	public String getEntityName() {
		return "metadata";
	}

}
