package frido.mvnrepo.indexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.Executor;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PomTest {

    Logger log = LoggerFactory.getLogger(PomTest.class);

    @Test
    public void testDownloader() {
        Executor executor = new DummyExecutor();
        HttpClient httpClient = new JerseyHttpClient();
        DummyPomHandler pomHandler = new DummyPomHandler();
        Downloader downloader = new Downloader(executor, httpClient, pomHandler);
        Document metadata = new Document().append("groupId", "antlr").append("artifactId", "antlr").append("version",
                "2.7.1");
        downloader.start(metadata);
        assertTrue(pomHandler.getContent().contains("<groupId>antlr</groupId>"));
        PomToJson converter = new PomToJson();
        Document pomJson = converter.toJsonMain(pomHandler.getContent());
        System.out.println(pomJson);
        assertEquals("antlr", pomJson.getString("GroupId"));
        assertEquals("antlr", pomJson.getString("ArtifactId"));
        assertEquals("2.7.1", pomJson.getString("Version"));
    }
}
