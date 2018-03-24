package frido.mvnrepo.indexer;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Downloader{

    Logger log = LoggerFactory.getLogger(Downloader.class);

    private Executor executor;
    private Client httpClient;
    private Consumer consumer;

    public Downloader(Executor executor, Client httpClient, Consumer consumer){
        this.executor = executor;
        this.httpClient = httpClient;
        this.consumer = consumer;
    }

    public void start(String url){
        //this.executor.execute(new DownloadTask(this, url));
        this.executor.execute( () -> {
            try {
                String content = this.httpClient.download(url);
                this.consumer.notify(content);
            } catch (Exception e) {
                log.error("Downloader - Task - Error", e);
            }
        });
    }
}