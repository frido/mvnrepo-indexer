package frido.mvnrepo.indexer;

import org.bson.Document;

public class Artifact {
    private Document data;

    Artifact(Document metadata){
        this.data = metadata;
    }
    
    public String getPomUrl() throws Exception {
        String groupId = data.getString("groupId");
        String artifactId = data.getString("artifactId");
        String version = data.getString("version");
        if (groupId == null || artifactId == null || version == null) {
            throw new Exception("Wrong metadata: " + data);// TODO: Custom exception
        }
        // TODO: base url configurable
        return "http://central.maven.org/maven2/" + groupId.replace(".", "/") + "/" + artifactId + "/" + version + "/"
                + artifactId + "-" + version + ".pom";
    }

    public static Artifact fromXml(String xml){
        // TODO
        return new Artifact(null);
    }
}