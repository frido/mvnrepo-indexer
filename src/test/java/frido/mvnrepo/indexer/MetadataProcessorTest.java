package frido.mvnrepo.indexer;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Executors;

import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.artifact.Metadata;
import frido.mvnrepo.indexer.artifact.MetadataHandler;
import frido.mvnrepo.indexer.artifact.MetadataProcessor;
import frido.mvnrepo.indexer.core.MockDatabase;
import frido.mvnrepo.indexer.core.download.ComparableExecutor;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;

public class MetadataProcessorTest {

    Logger log = LoggerFactory.getLogger(MetadataProcessorTest.class);

	@Test
    public void start() {
		MockDatabase database = new MockDatabase();
        DownloadExecutor executor =  new DownloadExecutor(new ComparableExecutor(5));
        MetadataHandler handler = new MetadataHandler(database);
        MetadataProcessor process = new MetadataProcessor(executor, handler);
        process.start("http://central.maven.org/maven2/ant/");
        process.waitForTerminate();
        assertEquals(35, database.getUpdated().size());
        for (Document doc : database.getUpdated()){
            log.debug("metadata: {}", doc);
            assertEquals("ant", doc.getString(Metadata.GROUP_ID));
        }
	}
}