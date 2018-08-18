package frido.mvnrepo.indexer;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;

import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.MockDatabase;
import frido.mvnrepo.indexer.core.NoThreadExecutor;
import frido.mvnrepo.indexer.core.download.MyExecutor;
import frido.mvnrepo.indexer.pom.PomProcessor;

public class PomProcessorTest {

    Logger log = LoggerFactory.getLogger(PomProcessor.class);

    @Test
    public void testDownloader() throws Exception {

        MockDatabase database = new MockDatabase();
        MyExecutor executor = new MyExecutor(new NoThreadExecutor());
        PomProcessor process2 = new PomProcessor(database, executor);
        process2.start();
        assertEquals(1, database.getUpdated().size());
        Document pom = database.getUpdated().get(0);
        assertEquals("org.apache.abdera", pom.getString("GroupId"));
        assertEquals("abdera", pom.getString("ArtifactId"));
        assertEquals("1.1.3", pom.getString("Version"));
    }
}
