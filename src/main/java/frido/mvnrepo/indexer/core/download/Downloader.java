package frido.mvnrepo.indexer.core.download;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.client.Client;

public class Downloader {

    Logger log = LoggerFactory.getLogger(Downloader.class);

    private Executor executor;
    private Client httpClient;

    public Downloader(Executor executor, Client httpClient) {
        this.executor = executor;
        this.httpClient = httpClient;
    }

    public void start(String url, Consumer consumer) {
        this.executor.execute(new PrioritableTask(url, httpClient, consumer));
    }
}