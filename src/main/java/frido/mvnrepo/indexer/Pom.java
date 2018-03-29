package frido.mvnrepo.indexer;

import org.bson.Document;

public class Pom {
    private Document data;

    public Pom(Document doc) {
        this.data = doc;
    }

    public static Pom valueOf(String xml){
        return new Pom(new PomConverter().valueOf(xml));
    }

    public Document getUniqFilter(){
        return new Document()
            .append("ArtifactId", data.getString("ArtifactId"))
            .append("GroupId", data.getString("GroupId"))
            .append("Version", data.getString("Version"));
    }

    public Document getDocument(){
        return this.data;
    }
}