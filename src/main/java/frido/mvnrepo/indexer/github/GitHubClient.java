package frido.mvnrepo.indexer.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.client.Client;
import frido.mvnrepo.indexer.core.client.ClientException;
import frido.mvnrepo.indexer.core.client.HttpClient;

public class GitHubClient implements Client {

	Logger log = LoggerFactory.getLogger(GitHubClient.class);

    private HttpClient client;

    public GitHubClient(HttpClient client){
        this.client = client;
    }

    public String download(String url) throws ClientException {
        log.trace("link:{}", url);
		return this.client.post("https://api.github.com/graphql", new GitHubUrl(url).query());
    }
}