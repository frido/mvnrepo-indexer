package frido.mvnrepo.indexer.core.client;

public interface Client{
    public String download(String url) throws ClientException;
}