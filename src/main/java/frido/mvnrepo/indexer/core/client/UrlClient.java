package frido.mvnrepo.indexer.core.client;

public class UrlClient implements Client {

    private HttpClient client;

    public UrlClient(HttpClient client){
        this.client = client;
    }

    public String download(String url) throws ClientException {
        return this.client.get(url);
    }
}