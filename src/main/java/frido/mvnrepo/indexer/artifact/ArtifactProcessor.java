package frido.mvnrepo.indexer.artifact;

import java.util.concurrent.ExecutorService;

import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.client.UrlClient;
import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.Crawler;
import frido.mvnrepo.indexer.core.download.CrawlerMatchHandler;
import frido.mvnrepo.indexer.core.download.Downloader;

public class ArtifactProcessor {
    Database db;
    ExecutorService executor;

    public ArtifactProcessor(Database database, ExecutorService executor){
        this.db = database;
        this.executor = executor;
    }

    // "http://central.maven.org/maven2/"
    public void start(String url) {
        CrawlerMatchHandler handler = new ArtifactHandler(db);
        UrlClient httpClient = new UrlClient(new JerseyHttpClient());
        Downloader downloader = new Downloader(executor, httpClient);
        Crawler c2 = new Crawler(downloader, "maven-metadata.xml", handler);
        c2.search(url);
    }
}