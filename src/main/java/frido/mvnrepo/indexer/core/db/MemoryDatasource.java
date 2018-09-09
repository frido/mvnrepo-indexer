package frido.mvnrepo.indexer.core.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

public class MemoryDatasource implements Datasource {

	private Map<String, List<Document>> memory = new HashMap<>();

	@Override
	public void save(String collection, Document record) {
		List<Document> list = memory.get(collection);
		if (list == null) {
			list = Collections.synchronizedList(new ArrayList<Document>());
			memory.put(collection, list);
		}
	}

	@Override
	public void update(String collection, Document record, Object identifier) {
		save(collection, record);
	}

	@Override
	public Iterable<Document> getAll(String collection) {
		List<Document> list = memory.get(collection);
		if (list == null) {
			list = Collections.synchronizedList(new ArrayList<Document>());
			memory.put(collection, list);
		}
		return memory.get(collection);
	}

	@Override
	public void close() throws IOException {

	}

}
