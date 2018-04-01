package frido.mvnrepo.indexer.artifact;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.CrawlerMatchHandler;

public class ArtifactHandler implements CrawlerMatchHandler {

    Logger log = LoggerFactory.getLogger(ArtifactHandler.class);
    private Database db;
    ExecutorService executor;

    public ArtifactHandler(Database database, ExecutorService executor){
        this.db = database;
        this.executor = executor;
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

	@Override
	public void terminate() {
        this.executor.shutdown();
        System.out.println("TERMINATE.........................");
    }
    
    public boolean isTerminated() {
        return this.executor.isTerminated();
    }

	public Executor getExecutor() {
		return this.executor;
	}

}