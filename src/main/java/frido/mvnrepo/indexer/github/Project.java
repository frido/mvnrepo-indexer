package frido.mvnrepo.indexer.github;

import org.bson.Document;

public class Project {
    private Document data;

    public Project(Document doc) {
        this.data = doc;
    }

    public Document getUniqFilter(){
        return new Document("owner", data.get("owner")).append("name", data.getString("name"));
    }

    public Document getDocument(){
        return this.data;
    }
}