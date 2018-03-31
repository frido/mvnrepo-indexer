package frido.mvnrepo.indexer.core.client;

public interface HttpClient {
    public String get(String url) throws ClientException;
    public String post(String url, String body) throws ClientException;
}