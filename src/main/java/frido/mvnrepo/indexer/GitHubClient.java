package frido.mvnrepo.indexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubClient implements Client {

	Logger log = LoggerFactory.getLogger(GitHubClient.class);

    private HttpClient client;

    GitHubClient(HttpClient client){
        this.client = client;
    }

    public String download(String url) throws ClientException {
        log.trace("link:{}", url);
		return this.client.post("https://api.github.com/graphql", new GitHubUrl(url).query());
    }
}