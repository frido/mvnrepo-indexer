package frido.mvnrepo.indexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bson.Document;
import org.junit.Test;

import frido.mvnrepo.indexer.artifact.Artifact;
import frido.mvnrepo.indexer.artifact.MetadataHandler;
import frido.mvnrepo.indexer.artifact.MetadataProcessor;
import frido.mvnrepo.indexer.core.MockDatabase;
import frido.mvnrepo.indexer.core.download.MyExecutor;

public class ArtifactProcessorTest {
	@Test
    public void start() {
		MockDatabase database = new MockDatabase();
        MyExecutor executor =  new MyExecutor(Executors.newFixedThreadPool(10));
        MetadataHandler handler = new MetadataHandler(database);
        MetadataProcessor process1 = new MetadataProcessor(executor, handler);
        process1.start("http://central.maven.org/maven2/ant/");
        while(!executor.isTerminated()){
            // wait while the process is active
        }
        assertEquals(35, database.getUpdated().size());
        for (Document doc : database.getUpdated()){
            assertEquals("ant", doc.getString(Artifact.GROUP_ID));
        }
	}
}