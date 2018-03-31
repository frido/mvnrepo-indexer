package frido.mvnrepo.indexer.core.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.client.Client;

public class PrioritableTask implements Prioritable {

    Logger log = LoggerFactory.getLogger(PrioritableTask.class);

    private int priority = 0;
    private Client httpClient;
    private Consumer consumer;
    private String url;

    public PrioritableTask(String url, Client httpClient, Consumer consumer) {
        this.httpClient = httpClient;
        this.consumer = consumer;
        this.url = url;
        for (char ch : url.toCharArray()) {
            if (ch == '/') {
                this.priority++;
            }
        }
    }

    @Override
    public void run() {
        try {
            String content = httpClient.download(url);
            consumer.notify(url, content);
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