package frido.mvnrepo.indexer;

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
        log.trace("match: {}", link);
        Document doc;
		try {
			doc = Metadata.valueOf(content);
		} catch (XmlParseException e) {
            log.error(link, e);
            return;
		}
        Artifact artifact = new Artifact(doc);
        if(artifact.isValid(link)){
            this.db.update("metadata", artifact.getUniqFilter(), artifact.getDocument());	
        }
	}

}