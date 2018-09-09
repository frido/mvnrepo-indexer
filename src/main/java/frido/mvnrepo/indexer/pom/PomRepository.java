package frido.mvnrepo.indexer.pom;

import org.bson.Document;

import frido.mvnrepo.indexer.core.db.AbstractRepository;
import frido.mvnrepo.indexer.core.db.Converter;
import frido.mvnrepo.indexer.core.db.Datasource;
import frido.mvnrepo.indexer.data.Pom;

public class PomRepository extends AbstractRepository<Pom> {

	public PomRepository(Datasource db, Converter<Pom, Document> converter) {
		super(db, converter);
	}

	@Override
	public String getEntityName() {
		return "pom";
	}

}
