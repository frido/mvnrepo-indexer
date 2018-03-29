package frido.mvnrepo.indexer;

public interface HttpClient {
    public String get(String url) throws ClientException;
    public String post(String url, String body) throws ClientException;
}