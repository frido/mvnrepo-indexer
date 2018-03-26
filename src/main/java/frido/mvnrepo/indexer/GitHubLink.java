package frido.mvnrepo.indexer;

// TODO: remove
@Deprecated
public class GitHubLink {
    //private String owner;
    //private String repo;

    public GitHubLink(String url){
        // String link1 = url + "/";
        // String part1 = link1.substring("https://github.com/".length()); // TODO: make it constant
        // this.owner = part1.substring(0, part1.indexOf("/"));
        // String part2 = part1.substring(owner.length() + 1);
        // this.repo = part2.substring(0, part2.indexOf("/")); 
    }
/*
    public String getOwner(){
        return this.owner;
    }

    public String getRepo(){
        return this.repo;
    }

    public String getQuery(){
        return "{ \"query\": \"query{repository(owner:\\\""+this.owner+"\\\", name:\\\""+this.repo+"\\\"){owner {login} name createdAt description homepageUrl pushedAt stargazers {totalCount}watchers{totalCount}forks{totalCount}}}\"}";
    }
    */
}