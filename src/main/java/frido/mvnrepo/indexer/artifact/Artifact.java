package frido.mvnrepo.indexer.artifact;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bson.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import frido.mvnrepo.indexer.pom.PomUrlBuilder;
import frido.mvnrepo.indexer.pom.PomUrlException;

public class Artifact {
	private static final String VERSION = "version";
	private static final String ARTIFACT_ID = "artifactId";
	private static final String GROUP_ID = "groupId";
    
    private Document data;

    public Artifact(Document metadata){
        data = metadata;
    }
    
    public String getPomUrl() throws PomUrlException {
        PomUrlBuilder urlBuilder = new PomUrlBuilder();
        return urlBuilder
            .group(data.getString(GROUP_ID))
            .artifact(data.getString(ARTIFACT_ID))
            .version(data.getString(VERSION))
            .build();
    }

    public Document getUniqFilter(){
        return new Document(GROUP_ID, data.getString(GROUP_ID))
            .append(ARTIFACT_ID, data.getString(ARTIFACT_ID))
            .append(VERSION, data.getString(VERSION));
    }

    public Document getDocument(){
        return data;
    }

    public boolean isValid(String link) {
        String[] linkParts = link.split("/");
        String artifactId = Arrays.asList(linkParts).get(linkParts.length - 2);
        return artifactId.equals(data.getString(ARTIFACT_ID));
    }
    
    public static org.bson.Document valueOf(String xml) throws XmlParseException {
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();
            org.bson.Document out = new org.bson.Document();

            NodeList metadata = doc.getElementsByTagName("metadata");
            for (int i = 0; i < metadata.getLength(); i++) {
                Element element = (Element) metadata.item(i);
                out.put(GROUP_ID, getContent(element, GROUP_ID));
                out.put(ARTIFACT_ID, getContent(element, ARTIFACT_ID));
                String v = getContent(element, VERSION);
                if (v != null) {
                    out.put(VERSION, v);
                }
            }

            NodeList versioning = doc.getElementsByTagName("versioning");
            org.bson.Document versioningDoc = new org.bson.Document();
            for (int i = 0; i < versioning.getLength(); i++) {
                Element element = (Element) versioning.item(i);
                versioningDoc.put("latest", getContent(element, "latest"));
                versioningDoc.put("release", getContent(element, "release"));
                versioningDoc.put("lastUpdated", getContent(element, "lastUpdated"));
                List<String> versions = new ArrayList<>();
                NodeList versionsElement = element.getElementsByTagName(VERSION);
                for (int j = 0; j < versionsElement.getLength(); j++) {
                    Element versionElement = (Element) versionsElement.item(j);
                    versions.add(versionElement.getTextContent());
                }
                versioningDoc.put("versions", versions);
            }
            out.put("versioning", versioningDoc);
            return out;
        } catch (Exception e) {
            throw new XmlParseException(e);
        }
    }

    private static String getContent(Element element, String tag) {
        if (element.getElementsByTagName(tag).getLength() > 0) {
            return element.getElementsByTagName(tag).item(0).getTextContent();
        }
        return null;
    }
}