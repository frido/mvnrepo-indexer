package frido.mvnrepo.indexer.core.download;

public interface Consumer{
    public void notify(String url, String content);
    public void error(Throwable e);
}