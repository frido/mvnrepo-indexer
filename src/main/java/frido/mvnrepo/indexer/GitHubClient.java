package frido.mvnrepo.indexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubClient implements Client {

    Logger log = LoggerFactory.getLogger(GitHubClient.class);

    private HttpClient client;

    GitHubClient(HttpClient client){
        this.client = client;
    }

    public String download(String url) throws Exception {
        log.info("link:" + url);
        String link1 = url + "/";
        String part1 = link1.substring("https://github.com/".length()); // TODO: make it constant
        String owner = part1.substring(0, part1.indexOf("/"));
        String part2 = part1.substring(owner.length() + 1);
        String repo = part2.substring(0, part2.indexOf("/")); 
        String query =  "{ \"query\": \"query{repository(owner:\\\""+owner+"\\\", name:\\\""+repo+"\\\"){owner {login} name createdAt description homepageUrl pushedAt stargazers {totalCount}watchers{totalCount}forks{totalCount}}}\"}";
        return this.client.post("https://api.github.com/graphql", query);
    }
}