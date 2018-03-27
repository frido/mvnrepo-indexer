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
        PrioritableUrlTask task = new PrioritableUrlTask("http://central.maven.org/maven2/activemq/activemq/", null, null);
        assertEquals(6, task.getPriority());
    }

    @Test
    public void testCrawler() throws Exception {
        Executor executor = new NoThreadExecutor();
        UrlClient httpClient = new UrlClient(new JerseyHttpClient());
        DummyMatchHandler matchHandler = new DummyMatchHandler();
        Downloader downloader = new Downloader(executor, httpClient);
        Crawler crawler = new Crawler(downloader, "maven-metadata.xml", matchHandler);
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
