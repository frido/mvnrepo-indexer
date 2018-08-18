package frido.mvnrepo.indexer.pom;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.artifact.Metadata;
import frido.mvnrepo.indexer.core.client.Client;
import frido.mvnrepo.indexer.core.client.DownloadClient;
import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLink;
import frido.mvnrepo.indexer.core.download.Downloader;

public class PomProcessor {
    static Logger log = LoggerFactory.getLogger(PomProcessor.class);
    private Database db;
    private DownloadExecutor executor;

    public PomProcessor(Database database, DownloadExecutor executor){
        this.db = database;
        this.executor = executor;
    }

    public void start() {
        Iterable<Document> list  = db.getAll("metadata");
        Consumer consumer = new PomHandler(db);
        Client httpClient = new DownloadClient(new JerseyHttpClient());
        Downloader loader = new Downloader(executor, httpClient);
        list.forEach(doc -> {
            String pomLink = null;
            try {
                pomLink = new Metadata(doc).getPomLink();
            } catch (PomUrlException e) {
                log.error("Artifact - pomLink", e);
                consumer.error(e);
                return;
			}
            loader.start(new DownloadLink(pomLink), consumer);
        });
    }
}