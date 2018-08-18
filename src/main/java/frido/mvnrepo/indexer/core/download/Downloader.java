package frido.mvnrepo.indexer.core.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.client.Client;

public class Downloader {

    Logger log = LoggerFactory.getLogger(Downloader.class);

    private DownloadExecutor executor;
    private Client httpClient;

    public Downloader(DownloadExecutor executor, Client httpClient) {
        this.executor = executor;
        this.httpClient = httpClient;
    }

    public void start(DownloadLink link, Consumer consumer) {
        this.executor.increment();
        this.executor.execute(new DownloadTask(link, httpClient, new Consumer(){
        
            @Override
            public void notify(DownloadLink url, String content) {
                consumer.notify(url, content);
                executor.decrementOrFinish();
            }
        
            @Override
            public void error(Throwable e) {
                consumer.error(e);
                executor.decrementOrFinish();
            }
        }));
    }
}