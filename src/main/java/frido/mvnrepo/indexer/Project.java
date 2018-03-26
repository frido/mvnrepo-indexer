package frido.mvnrepo.indexer;

import org.bson.Document;

public class Project {
    private Document data;

    public Project(Document doc) {
        this.data = doc;
    }
    /**
     * {
            "owner.login": "caskdata",
            "name": "cdap-ingest"
        }
     */
    public Document getUniqFilter(){
        return new Document("owner", data.get("owner")).append("name", data.getString("name"));
    }

    public Document getDocument(){
        return this.data;
    }
}