package frido.mvnrepo.indexer;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Executors;

import org.bson.Document;
import org.junit.Test;

import frido.mvnrepo.indexer.artifact.Metadata;
import frido.mvnrepo.indexer.artifact.MetadataHandler;
import frido.mvnrepo.indexer.artifact.MetadataProcessor;
import frido.mvnrepo.indexer.core.MockDatabase;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;

public class MetadataProcessorTest {
	@Test
    public void start() {
		MockDatabase database = new MockDatabase();
        DownloadExecutor executor =  new DownloadExecutor(Executors.newFixedThreadPool(10));
        MetadataHandler handler = new MetadataHandler(database);
        MetadataProcessor process = new MetadataProcessor(executor, handler);
        process.start("http://central.maven.org/maven2/ant/");
        process.waitForTerminate();
        assertEquals(35, database.getUpdated().size());
        for (Document doc : database.getUpdated()){
            assertEquals("ant", doc.getString(Metadata.GROUP_ID));
        }
	}
}