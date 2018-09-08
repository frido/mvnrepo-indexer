package frido.mvnrepo.indexer.core.db;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

// TODO: fix- remove dependency on org.bson.Document
public abstract class AbstractRepository<S extends Record> implements Repository<S> {

	Converter<S, Document> converter;
	private Datasource db;

	public AbstractRepository(Datasource db, Converter<S, Document> converter) {
		this.db = db;
		this.converter = converter;
	}

	@Override
	public void save(S record) {
		db.save(getEntityName(), converter.toEntity(record));
	}

	@Override
	public void update(S record) {
		db.update(getEntityName(), converter.toEntity(record), record.getId());
	}

	@Override
	public Iterable<S> getAll() {
		List<S> output = new LinkedList<S>();
		for (Document t : db.getAll(getEntityName())) {
			output.add(converter.toRecord(t));
		}
		return output;
	}

	public abstract String getEntityName();
}