package frido.mvnrepo.indexer.pom;

import org.bson.Document;

import frido.mvnrepo.indexer.core.db.Converter;
import frido.mvnrepo.indexer.data.Pom;

public class PomConverter implements Converter<Pom, Document> {

	@Override
	public Document toEntity(Pom record) {
		return record.getDocument();
	}

	@Override
	public Pom toRecord(Document doc) {
		return new Pom(doc);
	}
}
