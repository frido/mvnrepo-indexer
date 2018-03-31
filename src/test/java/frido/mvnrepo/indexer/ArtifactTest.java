package frido.mvnrepo.indexer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.junit.Test;

import frido.mvnrepo.indexer.artifact.Artifact;
import frido.mvnrepo.indexer.artifact.XmlParseException;

public class ArtifactTest {
    @Test
    @SuppressWarnings("unchecked")
    public void testMetadata() throws IOException, XmlParseException {
        String fileName = ClassLoader.getSystemResource("maven-metadata.xml").getFile();
        String content = new String(Files.readAllBytes(new File(fileName).toPath()));
        Document result = Artifact.valueOf(content);
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