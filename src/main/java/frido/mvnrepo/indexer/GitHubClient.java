package frido.mvnrepo.indexer;

public class GitHubClient implements Client {

    private HttpClient client;

    GitHubClient(HttpClient client){
        this.client = client;
    }

    public String download(String url) throws Exception {
        String link1 = url + "/";
        String part1 = link1.substring("https://github.com/".length()); // TODO: make it constant
        String owner = part1.substring(0, part1.indexOf("/"));
        String part2 = part1.substring(owner.length() + 1);
        String repo = part2.substring(0, part2.indexOf("/")); 
        String query =  "{ \"query\": \"query{repository(owner:\\\""+owner+"\\\", name:\\\""+repo+"\\\"){createdAt description homepageUrl pushedAt stargazers {totalCount}watchers{totalCount}forks{totalCount}}}\"}";
        return this.client.post("https://api.github.com/graphql", query);
    }
}