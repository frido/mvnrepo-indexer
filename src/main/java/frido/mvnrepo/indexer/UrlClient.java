package frido.mvnrepo.indexer;

public class UrlClient implements Client {

    private HttpClient client;

    UrlClient(HttpClient client){
        this.client = client;
    }

    public String download(String url) throws ClientException {
        return this.client.get(url);
    }
}