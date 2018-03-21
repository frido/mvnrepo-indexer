package frido.mvnrepo.indexer;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubLoader {

    Logger log = LoggerFactory.getLogger(GitHubLoader.class);

    private Executor executor;
    private Consumer consumer;
    private HttpClient httpClient;

    public GitHubLoader(Executor executor, Consumer consumer, HttpClient httpClient) {
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
        return httpClient.callGitHub(new GitHubLink(link).getQuery());
    }
}