package frido.mvnrepo.indexer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// TODO: adapter pattern for conversion from xml to json?
public class Metadata {
    /**
     * Parse metadata Json from content.
     */
    public static org.bson.Document valueOf(String xml) {
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = docBuilder.parse(
                new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.getDocumentElement().normalize();
        org.bson.Document out = new org.bson.Document();

        // TODO: field names as constants
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
        //TODO: set default version the first/last one from list if version is empty
        out.put("versioning", versioningDoc);
        return out;
    }

    // FIXME: duplicitne volanie getElement...
    private static String getContent(Element element, String tag) {
        if (element.getElementsByTagName(tag).getLength() > 0) {
            return element.getElementsByTagName(tag).item(0).getTextContent();
        }
        return null;
    }
}