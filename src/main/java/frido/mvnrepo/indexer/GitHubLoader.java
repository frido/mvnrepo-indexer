package frido.mvnrepo.indexer;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: remove
public class GitHubLoader {

    Logger log = LoggerFactory.getLogger(GitHubLoader.class);

    private Executor executor;
    private Consumer consumer;
    private Client httpClient;

    public GitHubLoader(Executor executor, Consumer consumer, Client httpClient) {
        this.executor = executor;
        this.consumer = consumer;
        this.httpClient = httpClient;
    }

    public void start(String gitHubLink) {
        this.executor.execute(new GitHubLoaderTask(this, gitHubLink));
    }

    public void notify(String payload) {
        this.consumer.notify(payload);
    }

    public String download(String link) throws Exception {
        log.trace("download: {}-{}", link);
        //return httpClient.callGitHub(new GitHubLink(link).getQuery());
        return this.httpClient.download(link);
    }
}