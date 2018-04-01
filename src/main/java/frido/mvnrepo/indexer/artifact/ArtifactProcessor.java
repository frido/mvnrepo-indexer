package frido.mvnrepo.indexer.artifact;

import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.client.UrlClient;
import frido.mvnrepo.indexer.core.download.Crawler;
import frido.mvnrepo.indexer.core.download.Downloader;

public class ArtifactProcessor {
    ArtifactHandler handler;

    public ArtifactProcessor(ArtifactHandler handler){
        this.handler = handler;
    }

    // "http://central.maven.org/maven2/"
    public void start(String url) {
        UrlClient httpClient = new UrlClient(new JerseyHttpClient());
        Downloader downloader = new Downloader(handler.getExecutor(), httpClient);
        Crawler c2 = new Crawler(downloader, "maven-metadata.xml", handler);
        c2.search(url);
    }
}