package frido.mvnrepo.indexer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubLoaderTask implements Runnable {

    Logger log = LoggerFactory.getLogger(GitHubLoaderTask.class);

    private String gitHubLink;
    private GitHubLoader ctx;
    private String user;
    private String repo;

    public GitHubLoaderTask(String gitHubLink, GitHubLoader ctx) {
        this.gitHubLink = gitHubLink;
        this.ctx = ctx;
        initGitHubInfo(gitHubLink);
    }

    private void initGitHubInfo(String link){
        String link1 = link + "/";
        String part1 = link1.substring("https://github.com/".length()); // TODO: make it constant
        this.user = part1.substring(0, part1.indexOf("/"));
        String part2 = part1.substring(this.user.length() + 1);
        this.repo = part2.substring(0, part2.indexOf("/")); 
        log.trace("initGitHubInfo:{}-{}-{}", this.gitHubLink, this.user, this.repo);
    }

    @Override
    public void run() {
        try {
            String content = download(this.user, this.repo);
            this.ctx.notify(this.gitHubLink, content);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    //TODO: duplicitny kod z crawler-a
    private String download(String username, String repo) throws Exception {
        log.debug("download({}-{})", username, repo); // TODO: zjedotit debug vypisy (jednotny format)
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("frido", System.getenv().get("GITHUB_KEY")));
        WebResource webResource = client.resource("https://api.github.com/graphql");
        String query = "{ \"query\": \"query{repository(owner:\\\""+this.user+"\\\", name:\\\""+this.repo+"\\\"){createdAt description homepageUrl pushedAt stargazers {totalCount}watchers{totalCount}forks{totalCount}}}\"}";
        ClientResponse response = webResource.post(ClientResponse.class, query);
        if (response.getStatus() != 200) {
            throw new Exception("Failed : HTTP error code : " + response.getStatus() + " -> " + response.getEntity(String.class));
        }
        String output = response.getEntity(String.class);
        return output;
    }

}