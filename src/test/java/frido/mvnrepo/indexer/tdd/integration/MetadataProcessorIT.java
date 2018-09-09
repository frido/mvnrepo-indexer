package frido.mvnrepo.indexer.tdd.integration;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.download.ComparableExecutor;
import frido.mvnrepo.indexer.core.download.DownloadClient;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLinkClient;
import frido.mvnrepo.indexer.data.Metadata;
import frido.mvnrepo.indexer.tdd.helper.MockDatabase;

public class MetadataProcessorIT {

	Logger log = LoggerFactory.getLogger(MetadataProcessorIT.class);

	@Test
	public void start() {
		DownloadExecutor executor = new DownloadExecutor(new ComparableExecutor(5));
		PomExtractorDatabase database = new PomExtractorDatabase();
		DownloadClient client = new DownloadLinkClient(new JerseyHttpClient());
		// MetadataProcessor processor = new MetadataProcessor(database, client,
		// executor);
		// processor.start("http://central.maven.org/maven2/ant/");

//		
//		Crawler clawler = new Crawler(, executor, consumer);
//		clawler.search(new DownloadLink("http://central.maven.org/maven2/ant/"), "maven-metadata.xml");

		executor.waitForTerminate();

		assertEquals(35, database.getUpdated().size());
		for (Document doc : database.getUpdated()) {
			log.info("metadata: {}", doc);
			assertEquals("ant", doc.getString(Metadata.GROUP_ID));
		}
	}

	class PomExtractorDatabase extends MockDatabase {

		private List<Document> uptaded = Collections.synchronizedList(new ArrayList<>());

		public void update(String collection, Document query, Document newOne) {
			uptaded.add(newOne);
		}

		public List<Document> getUpdated() {
			return uptaded;
		}

	}

}
