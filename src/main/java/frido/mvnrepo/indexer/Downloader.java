package frido.mvnrepo.indexer;

import java.util.concurrent.Executor;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Downloader{

    Logger log = LoggerFactory.getLogger(Downloader.class);

    private Executor executor;
    private HttpClient httpClient;
    private Consumer consumer;

    public Downloader(Executor executor, HttpClient httpClient, Consumer consumer){
        this.executor = executor;
        this.httpClient = httpClient;
        this.consumer = consumer;
    }

    public void start(Document metadata){
        this.executor.execute(new DownloadTask(this, metadata));
    }

    public void notify(String content/*Document metadata, String projectUrl*/){
        this.consumer.notify(content);
    }
  
    public String download(String link) {
        log.trace("download: {}", link);
        return httpClient.get(link);
    }
}