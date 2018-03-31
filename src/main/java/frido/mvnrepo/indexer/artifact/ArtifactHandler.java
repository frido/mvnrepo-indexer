package frido.mvnrepo.indexer.artifact;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.CrawlerMatchHandler;

class ArtifactHandler implements CrawlerMatchHandler {

    Logger log = LoggerFactory.getLogger(ArtifactHandler.class);
    private Database db;

    ArtifactHandler(Database database){
        this.db = database;
    }

	@Override
	public void match(String link, String content) {
        log.trace("match: {}", link);
        Document doc;
		try {
			doc = Artifact.valueOf(content);
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