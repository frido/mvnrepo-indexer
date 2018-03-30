package frido.mvnrepo.indexer;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Executor;

import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PomTest {

    Logger log = LoggerFactory.getLogger(PomTest.class);

    @Test
    public void testDownloader() throws Exception {
        Executor executor = new NoThreadExecutor();
        UrlClient httpClient = new UrlClient(new JerseyHttpClient());
        DummyPomHandler pomHandler = new DummyPomHandler();
        Downloader downloader = new Downloader(executor, httpClient);
        downloader.start("http://central.maven.org/maven2/org/apache/abdera/abdera/1.1.3/abdera-1.1.3.pom", pomHandler);
        Pom pom = Pom.valueOf(pomHandler.getContent());
        Document pomJson = pom.getDocument();
        System.out.println(pomJson);
        assertEquals("org.apache.abdera", pomJson.getString("GroupId"));
        assertEquals("abdera", pomJson.getString("ArtifactId"));
        assertEquals("1.1.3", pomJson.getString("Version"));
    }
}
