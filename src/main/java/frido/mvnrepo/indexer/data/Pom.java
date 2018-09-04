package frido.mvnrepo.indexer.data;

import org.bson.Document;

import frido.mvnrepo.indexer.pom.PomConverter;

public class Pom {
	private static final String VERSION = "Version";
	private static final String GROUP_ID = "GroupId";
	private static final String ARTIFACT_ID = "ArtifactId";
	private Document data;

	public Pom(Document doc) {
		this.data = doc;
	}

	public static Pom valueOf(String xml) {
		return new Pom(new PomConverter().valueOf(xml)); // TODO: better converter call
	}

	public Document getUniqFilter() {
		return new Document().append(ARTIFACT_ID, data.getString(ARTIFACT_ID))
				.append(GROUP_ID, data.getString(GROUP_ID)).append(VERSION, data.getString(VERSION));
	}

	public Document getDocument() {
		return this.data;
	}
}