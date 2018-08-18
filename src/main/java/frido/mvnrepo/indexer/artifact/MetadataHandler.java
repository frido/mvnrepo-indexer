package frido.mvnrepo.indexer.artifact;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.CrawlerMatchHandler;

public class MetadataHandler implements CrawlerMatchHandler {

    Logger log = LoggerFactory.getLogger(MetadataHandler.class);
    private Database db;
    ExecutorService executor;

    public MetadataHandler(Database database){
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
/*
    @Override
    //TODO: move to own interface
	public void terminate() {
        this.executor.shutdown();
    }
    
    public boolean isTerminated() {
        return this.executor.isTerminated();
    }

	public Executor getExecutor() {
		return this.executor;
	}
*/
}