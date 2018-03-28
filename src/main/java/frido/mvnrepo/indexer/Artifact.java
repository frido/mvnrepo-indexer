package frido.mvnrepo.indexer;

import org.bson.Document;

public class Artifact {
    private Document data;

    Artifact(Document metadata){
        this.data = metadata;
    }
    
    public String getPomUrl() throws PomUrlException {
        PomUrlBuilder urlBuilder = new PomUrlBuilder();
        return urlBuilder
            .group(data.getString("groupId"))
            .artifact(data.getString("artifactId"))
            .version(data.getString("version"))
            .build();
    }

    public static Artifact fromXml(String xml){
        // TODO
        return new Artifact(null);
    }
}