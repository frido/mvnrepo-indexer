package frido.mvnrepo.indexer;

import java.util.Arrays;

import org.bson.Document;

public class Artifact {
	private static final String VERSION = "version";
	private static final String ARTIFACT_ID = "artifactId";
	private static final String GROUP_ID = "groupId";
    
    private Document data;

    Artifact(Document metadata){
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
        if(artifactId.equals(data.getString(ARTIFACT_ID))){
            return true;
        }
        return false;
    }
}