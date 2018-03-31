package frido.mvnrepo.indexer.pom;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.artifact.Artifact;
import frido.mvnrepo.indexer.core.client.Client;
import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.client.UrlClient;
import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.Downloader;

public class PomProcessor {
    static Logger log = LoggerFactory.getLogger(PomProcessor.class);
    Database db;

    public PomProcessor(Database database){
        this.db = database;
    }

    public void start() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Client httpClient = new UrlClient(new JerseyHttpClient());
        Iterable<Document> metadatas = db.getAll("metadata");
        Iterator<Document> it = metadatas.iterator();
        int size = 0;
        while(it.hasNext()){
            it.next();
            size++;
        }
        Consumer pomHandler = new PomHandler(db, executor, size);
        Downloader downloader = new Downloader(executor, httpClient);
        it = metadatas.iterator();
        while(it.hasNext()){
            String pomUrl = null;
			try {
                pomUrl = new Artifact(it.next()).getPomUrl();
            } catch (Exception e) {
                log.error("Artifact - PomUrl", e);
                pomHandler.error(e);
                continue;
			}
            downloader.start(pomUrl, pomHandler);
        }
    }
}