package frido.mvnrepo.indexer.core.download;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.client.Client;

public class Downloader {

    Logger log = LoggerFactory.getLogger(Downloader.class);

    private MyExecutor executor;
    private Client httpClient;

    public Downloader(MyExecutor executor, Client httpClient) {
        this.executor = executor;
        this.httpClient = httpClient;
    }

    public void start(String url, Consumer consumer) {
        this.executor.increment();
        this.executor.execute(new PrioritableTask(url, httpClient, new Consumer(){
        
            @Override
            public void notify(String url, String content) {
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