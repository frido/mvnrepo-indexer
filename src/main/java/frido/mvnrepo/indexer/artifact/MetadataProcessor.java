package frido.mvnrepo.indexer.artifact;

import frido.mvnrepo.indexer.core.client.DownloadClient;
import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.download.Crawler;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLink;
import frido.mvnrepo.indexer.core.download.Downloader;

public class MetadataProcessor {
    MetadataHandler handler;
    DownloadExecutor executor;

    public MetadataProcessor(DownloadExecutor executor, MetadataHandler handler){
        this.handler = handler;
        this.executor = executor;
    }

    // "http://central.maven.org/maven2/"
    public void start(String url) {
        DownloadClient httpClient = new DownloadClient(new JerseyHttpClient());
        Downloader downloader = new Downloader(executor, httpClient);
        Crawler c2 = new Crawler(downloader, "maven-metadata.xml", handler);
        c2.search(new DownloadLink(url));
    }

    public void waitForTerminate(){
        while(!executor.isTerminated()){
            // wait while the process is active
        }
    }
}