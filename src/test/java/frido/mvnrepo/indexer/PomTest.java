package frido.mvnrepo.indexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.bson.Document;
import org.junit.jupiter.api.Test;
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
        Document metadata = new Document().append("groupId", "antlr").append("artifactId", "antlr").append("version",
                "2.7.1");
        downloader.start("http://central.maven.org/maven2/org/apache/activemq/activeio-core/3.1.4/activeio-core-3.1.4.pom", pomHandler);
        assertTrue(pomHandler.getContent().contains("<groupId>org.apache.activemq</groupId>"));
        //PomToJson converter = new PomToJson();
        XmlMapper xmlMapper = new XmlMapper();
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
        };
        Map<String, Object> map = xmlMapper.readValue(pomHandler.getContent(), typeRef);
        Document pomJson = new Document(map);
        System.out.println("......:"+pomJson);
        //Document pomJson = converter.toJsonMain(pomHandler.getContent());
        assertEquals("org.apache.activemq", pomJson.getString("groupId"));
        assertEquals("activeio-parent", pomJson.getString("artifactId"));
        assertEquals("3.1.4", pomJson.getString("version"));
    }
}
