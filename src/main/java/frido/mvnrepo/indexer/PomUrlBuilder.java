package frido.mvnrepo.indexer;

public class PomUrlBuilder {
	private static final String HTTP_CENTRAL_MAVEN_ORG_MAVEN2 = "http://central.maven.org/maven2/";
	private String group;
    private String artifact;
    private String version;

	public PomUrlBuilder group(String group) {
        this.group = group;
		return this;
    }
    
    public PomUrlBuilder artifact(String artifact) {
        this.artifact = artifact;
        return this;
    }
    
    public PomUrlBuilder version(String version) {
        this.version = version;
        return this;
    }
    
    public String build() throws PomUrlException{
        if (group == null || artifact == null || version == null) {
            throw new PomUrlException(String.format("PomUrlException: group=%s, artifact=%s, version=%s", group, artifact, version));
        }
        return HTTP_CENTRAL_MAVEN_ORG_MAVEN2 + group.replace(".", "/") + "/" + artifact + "/" + version + "/"
                + artifact + "-" + version + ".pom";
    }

    
}