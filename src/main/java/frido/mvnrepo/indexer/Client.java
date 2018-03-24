package frido.mvnrepo.indexer;

public interface Client{
    public String download(String url) throws Exception;
}