package frido.mvnrepo.indexer;

import java.util.Arrays;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class StoreMatchHandler implements CrawlerMatchHandler {

    Logger log = LoggerFactory.getLogger(StoreMatchHandler.class);
    private Database db;

    StoreMatchHandler(Database database){
        this.db = database;
    }

	@Override
	public void match(String link, String content) {
        Document doc = Metadata.valueOf(content); // TODO: we dont want static methods???
        Document query = new Document();
        query
            .append("groupId", doc.getString("groupId"))
            .append("artifactId", doc.getString("artifactId"))
            .append("version", doc.getString("version"));
        log.info("match: {}", doc);
        String[] linkParts = link.split("/");
        String artifactId = Arrays.asList(linkParts).get(linkParts.length - 2);
        if(!artifactId.equals(doc.getString("artifactId"))){
            System.out.println(".....:"+artifactId+"::"+doc.getString("artifactId"));
        }else{
            this.db.update("metadata", query, doc);	
        }
        
	}

}