package frido.mvnrepo.indexer.artifact;

import java.util.concurrent.ExecutorService;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.CrawlerMatchHandler;
import frido.mvnrepo.indexer.core.download.DownloadLink;

public class MetadataHandler implements CrawlerMatchHandler {

    Logger log = LoggerFactory.getLogger(MetadataHandler.class);
    private Database db;
    ExecutorService executor;

    public MetadataHandler(Database database){
        this.db = database;
    }

	@Override
	public void match(DownloadLink link, String content) {
        log.trace("match: {}", link);
        Document doc;
		try {
			doc = Metadata.valueOf(content);
		} catch (XmlParseException e) {
            log.error(link.toString(), e);
            return;
		}
        Metadata artifact = new Metadata(doc);
        if(artifact.isValid(link.getLink())){
            this.db.update("metadata", artifact.getUniqFilter(), artifact.getDocument());	
        }
	}
}