package frido.mvnrepo.indexer.core.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class FileDatasource implements Datasource {

	Logger log = LoggerFactory.getLogger(FileDatasource.class);

	private Map<String, FileWriter> writers = new HashMap<>();

	public FileDatasource() throws IOException {
	}

	@Override
	public void save(String collection, Document record) {
		FileWriter writer = getOrCreateWriter(collection);
		if (writer != null) {
			try {
				writer.write(record.toJson() + "\n"); // TODO: use system end line
			} catch (IOException e) {
				log.error("", e);
			}
		}
	}

	@Override
	public void update(String collection, Document record, Object identifier) {
		save(collection, record);

	}

	@Override
	public Iterable<Document> getAll(String collection) {
		List<Document> output = new LinkedList<Document>();
		try (BufferedReader br = new BufferedReader(new FileReader(collection + ".db"))) {
			String line;
			while ((line = br.readLine()) != null) {
				log.info(line);
				try {
					BasicDBObject record = (BasicDBObject) JSON.parse(line);
					output.add(new Document(record));
				} catch (Exception e) {
					log.error("JSON.parse(line) ERROR");
				}
			}
		} catch (FileNotFoundException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}
		return output;
	}

	@Override
	public void close() throws IOException {
		System.out.println("Closing...");
		for (FileWriter fw : writers.values()) {
			fw.close();
		}
	}

	private FileWriter getOrCreateWriter(String collection) {
		FileWriter writer = writers.get(collection);
		if (writer == null) {
			try {
				writer = new FileWriter(collection + ".db", true);
				writers.put(collection, writer);
			} catch (IOException e) {
				log.error("", e);
			}
		}
		return writer;
	}

}