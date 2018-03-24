package frido.mvnrepo.indexer;

public interface HttpClient {
    public String get(String url);
    public String post(String url, String body) throws Exception;
}