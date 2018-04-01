package frido.mvnrepo.indexer;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import frido.mvnrepo.indexer.artifact.ArtifactHandler;
import frido.mvnrepo.indexer.artifact.ArtifactProcessor;
import frido.mvnrepo.indexer.core.MockDatabase;

public class ArtifactProcessorTest {
	@Test
    public void start() {
		MockDatabase database = new MockDatabase();
        //ExecutorService executor =  new NoThreadExecutor();
        ExecutorService executor =  Executors.newFixedThreadPool(10);
        ArtifactHandler handler = new ArtifactHandler(database, executor);
        ArtifactProcessor process1 = new ArtifactProcessor(handler);
        process1.start("http://central.maven.org/maven2/ant/");
        while(!handler.isTerminated()){
            // wait while the process is active
        }
        assertEquals(35, database.getUpdated().size());
	}
}