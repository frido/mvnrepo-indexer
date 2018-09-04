package frido.mvnrepo.indexer.data;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bson.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import frido.mvnrepo.indexer.metadata.XmlParseException;

public class Metadata {
	private static final String IDENTIFIER = "identifier";
	private static final String VERSIONS2 = "versions";
	private static final String LAST_UPDATED = "lastUpdated";
	private static final String RELEASE = "release";
	private static final String LATEST = "latest";
	public static final String REPO = "repo";
	public static final String VERSION = "version";
	public static final String ARTIFACT_ID = "artifactId";
	public static final String GROUP_ID = "groupId";

	private Document data;

	public Metadata(Document metadata) {
		data = metadata;
	}

	public String getRepo() {
		return data.getString(REPO);
	}

	public String getGroup() {
		return data.getString(GROUP_ID);
	}

	public String getArtifact() {
		return data.getString(ARTIFACT_ID);
	}

	public String getVersion() {
		return Optional.ofNullable(data.getString(VERSION)).orElse(data.getString(LATEST));
	}

	public String getIdentifier() {
		return data.getString(IDENTIFIER);
	}

	public org.bson.Document getIdentifierFilter() {
		return new Document(IDENTIFIER, data.getString(IDENTIFIER));
	}

	public Document getDocument() {
		return data;
	}

	public boolean isValid(String link) {
		String[] linkParts = link.split("/");
		String artifactId = Arrays.asList(linkParts).get(linkParts.length - 2);
		return artifactId.equals(data.getString(ARTIFACT_ID));
	}

	public static Metadata valueOf(Document doc) {
		return new Metadata(doc);
	}

	public static org.bson.Document valueOf(String xml, String repo) throws XmlParseException {
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder
					.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

			doc.getDocumentElement().normalize();
			org.bson.Document out = new org.bson.Document();

			NodeList metadata = doc.getElementsByTagName("metadata");
			if (metadata.getLength() > 0) {
				Element elementM = (Element) metadata.item(0);
				out.put(GROUP_ID, getContent(elementM, GROUP_ID));
				out.put(ARTIFACT_ID, getContent(elementM, ARTIFACT_ID));
				out.put(VERSION, getContent(elementM, VERSION));
//				String v = getContent(element, VERSION);
//				if (v != null) {
//					out.put(VERSION, v);
//				}
				NodeList versioning = doc.getElementsByTagName("versioning");
				if (versioning.getLength() > 0) {
					Element elementV = (Element) versioning.item(0);
					out.put(LATEST, getContent(elementV, LATEST));
					out.put(RELEASE, getContent(elementV, RELEASE));
					out.put(LAST_UPDATED, getContent(elementV, LAST_UPDATED));
					List<String> versions = new ArrayList<>();
					NodeList versionsElement = elementV.getElementsByTagName(VERSION);
					for (int j = 0; j < versionsElement.getLength(); j++) {
						Element versionElement = (Element) versionsElement.item(j);
						versions.add(versionElement.getTextContent());
					}
					out.put(VERSIONS2, versions);
				}
				out.append(REPO, repo);
				out.append(IDENTIFIER, createIdentifier(out));
			}
			return out;
		} catch (Exception e) {
			throw new XmlParseException(e);
		}
	}

	private static String createIdentifier(org.bson.Document doc) {
		String group = doc.getString(GROUP_ID);
		String artifact = doc.getString(ARTIFACT_ID);
		String version = Optional.ofNullable(doc.getString(VERSION)).orElse(doc.getString(LATEST));
		return String.format("%s:%s:%s", group, artifact, version);
	}

	private static String getContent(Element element, String tag) {
		if (element.getElementsByTagName(tag).getLength() > 0) {
			return element.getElementsByTagName(tag).item(0).getTextContent();
		}
		return null;
	}
}