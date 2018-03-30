package frido.mvnrepo.indexer;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Metadata {
    static Logger log = LoggerFactory.getLogger(Metadata.class);

    public static org.bson.Document valueOf(String xml) throws XmlParseException {
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();
            org.bson.Document out = new org.bson.Document();

            NodeList metadata = doc.getElementsByTagName("metadata");
            for (int i = 0; i < metadata.getLength(); i++) {
                Element element = (Element) metadata.item(i);
                out.put("groupId", getContent(element, "groupId"));
                out.put("artifactId", getContent(element, "artifactId"));
                String v = getContent(element, "version");
                if (v != null) {
                    out.put("version", v);
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
                NodeList versionsElement = element.getElementsByTagName("version");
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