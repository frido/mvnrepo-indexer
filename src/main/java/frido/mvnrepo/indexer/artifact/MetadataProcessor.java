package frido.mvnrepo.indexer.artifact;

import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.client.UrlClient;
import frido.mvnrepo.indexer.core.download.Crawler;
import frido.mvnrepo.indexer.core.download.Downloader;
import frido.mvnrepo.indexer.core.download.MyExecutor;

public class MetadataProcessor {
    MetadataHandler handler;
    MyExecutor executor;

    public MetadataProcessor(MyExecutor executor, MetadataHandler handler){
        this.handler = handler;
        this.executor = executor;
    }

    // "http://central.maven.org/maven2/"
    public void start(String url) {
        UrlClient httpClient = new UrlClient(new JerseyHttpClient());
        Downloader downloader = new Downloader(executor, httpClient);
        Crawler c2 = new Crawler(downloader, "maven-metadata.xml", handler);
        c2.search(url);
    }
}