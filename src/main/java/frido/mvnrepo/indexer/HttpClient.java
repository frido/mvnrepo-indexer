package frido.mvnrepo.indexer;

// TODO: two different interfaces http a github client
public interface HttpClient {
    public String get(String url);
    public String callGitHub(String query) throws Exception;
}