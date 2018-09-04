package frido.mvnrepo.indexer.pom;

public class PomLinkBuilder {
	private String group;
	private String artifact;
	private String version;
	private String repo;

	public PomLinkBuilder repo(String repo) {
		this.repo = repo;
		return this;
	}

	public PomLinkBuilder group(String group) {
		this.group = group;
		return this;
	}

	public PomLinkBuilder artifact(String artifact) {
		this.artifact = artifact;
		return this;
	}

	public PomLinkBuilder version(String version) {
		this.version = version;
		return this;
	}

	public String build() throws PomUrlException {
		if (group == null || artifact == null || version == null) {
			throw new PomUrlException(
					String.format("PomUrlException: group=%s, artifact=%s, version=%s", group, artifact, version));
		}
		return repo + group.replace(".", "/") + "/" + artifact + "/" + version + "/" + artifact + "-" + version
				+ ".pom";
	}

}