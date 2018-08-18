package frido.mvnrepo.indexer.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.client.Client;
import frido.mvnrepo.indexer.core.client.ClientException;
import frido.mvnrepo.indexer.core.client.HttpClient;
import frido.mvnrepo.indexer.core.download.DownloadLink;

public class GitHubClient implements Client {

	Logger log = LoggerFactory.getLogger(GitHubClient.class);

    private HttpClient client;

    public GitHubClient(HttpClient client){
        this.client = client;
    }

    public String download(DownloadLink link) throws ClientException {
        log.trace("link:{}", link);
		return this.client.post("https://api.github.com/graphql", new GitHubUrl(link).query());
    }
}