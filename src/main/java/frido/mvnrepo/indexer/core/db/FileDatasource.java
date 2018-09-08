package frido.mvnrepo.indexer.core.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileDatasource implements Datasource {

	Logger log = LoggerFactory.getLogger(FileDatasource.class);

	private File file;
	BufferedWriter writer;

	// TODO: closing writer!!!
	// TODO: handle different collections
	public FileDatasource(String pathname) throws FileNotFoundException {
		file = new File(pathname);
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
	}

	@Override
	public void save(String collection, Document record) {
		try {
			log.trace(record.toJson()); // TODO: fix
			writer.write(record.toJson());
			writer.write("\n");
		} catch (IOException e) {
			log.error("", e);
		}
	}

	@Override
	public void update(String collection, Document record, Object identifier) {
		save(collection, record);

	}

	@Override
	public Iterable<Document> getAll(String collection) {
		throw new UnsupportedOperationException();
	}

}