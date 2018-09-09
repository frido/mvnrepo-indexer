package frido.mvnrepo.indexer.data;

import org.bson.Document;

import frido.mvnrepo.indexer.core.db.Record;
import frido.mvnrepo.indexer.pom.PomHelper;

public class Pom implements Record {
	private static final String VERSION = "Version";
	private static final String GROUP_ID = "GroupId";
	private static final String ARTIFACT_ID = "ArtifactId";
	private Document data;

	public Pom(Document doc) {
		this.data = doc;
	}

	public static Pom valueOf(String xml) {
		return new Pom(new PomHelper().valueOf(xml)); // TODO: better converter call
	}

	public Document getDocument() {
		return this.data;
	}

	@Override
	public String getId() {
		return String.format("%s:%s:%s", data.getString(GROUP_ID), data.getString(ARTIFACT_ID),
				data.getString(VERSION));
	}

	public String getGitUrl() {
		return data.getString("Url");
	}
}