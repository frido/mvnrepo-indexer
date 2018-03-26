package frido.mvnrepo.indexer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataTest {

    Logger log = LoggerFactory.getLogger(MetadataTest.class);

    @Test
    public void testTaskPriority() {
        CrawlerMatchHandler matchHandler = new DummyMatchHandler();
        Executor executor = new NoThreadExecutor();
        HttpClient httpClient = new JerseyHttpClient();
        Crawler crawler = new Crawler("test", matchHandler, executor, httpClient);
        Task task = new Task(crawler, "http://central.maven.org/maven2/activemq/activemq/");
        assertEquals(6, task.getPriority());
    }

    @Test
    public void testCrawler() {
        DummyMatchHandler matchHandler = new DummyMatchHandler();
        Executor executor = new NoThreadExecutor();
        HttpClient httpClient = new JerseyHttpClient();
        Crawler crawler = new Crawler("maven-metadata.xml", matchHandler, executor, httpClient);
        crawler.search("http://central.maven.org/maven2/abbot/");
        assertEquals(4, matchHandler.getCount());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMetadata() throws IOException {
        String fileName = ClassLoader.getSystemResource("maven-metadata.xml").getFile();
        String content = new String(Files.readAllBytes(new File(fileName).toPath()));
        Document result = Metadata.valueOf(content);
        assertEquals("abbot", result.getString("groupId"));
        assertEquals("abbot", result.getString("artifactId"));
        Document versioning = (Document) result.get("versioning");
        assertEquals("1.4.0", versioning.getString("latest"));
        assertEquals("1.4.0", versioning.getString("release"));
        assertEquals("20150924141841", versioning.getString("lastUpdated"));
        List<String> versionsBson = versioning.get("versions", List.class);
        List<String> versions = new ArrayList<>();
        for (String version : versionsBson) {
            versions.add(version);
        }
        assertArrayEquals(Arrays.asList("0.12.3", "0.13.0", "1.4.0").toArray(), versions.toArray());
    }
}
