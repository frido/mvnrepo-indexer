package frido.mvnrepo.indexer.core.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.client.Client;

public class DownloadTask implements Prioritable {

    Logger log = LoggerFactory.getLogger(DownloadTask.class);

    private int priority = 0;
    private Client httpClient;
    private Consumer consumer;
    private DownloadLink link;

    public DownloadTask(DownloadLink link, Client httpClient, Consumer consumer) {
        this.httpClient = httpClient;
        this.consumer = consumer;
        this.link = link;
        this.priority = link.getDeep();        
    }

    @Override
    public void run() {
        try {
            String content = httpClient.download(link);
            consumer.notify(link, content);
        } catch (Exception e) {
            log.error("Downloader - Task - Error", e);
            consumer.error(e);
        }
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

}