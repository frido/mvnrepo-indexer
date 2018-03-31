package frido.mvnrepo.indexer;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;

import org.junit.Test;

import frido.mvnrepo.indexer.artifact.ArtifactProcessor;
import frido.mvnrepo.indexer.core.MockDatabase;
import frido.mvnrepo.indexer.core.NoThreadExecutor;

public class ArtifactProcessorTest {
	@Test
    public void start() {
		MockDatabase database = new MockDatabase();
        ExecutorService executor =  new NoThreadExecutor();
        ArtifactProcessor process1 = new ArtifactProcessor(database, executor);
        process1.start("http://central.maven.org/maven2/abbot/");
        assertEquals(2, database.getUpdated().size());
	}
}