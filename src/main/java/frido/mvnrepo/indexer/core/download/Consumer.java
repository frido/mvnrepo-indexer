package frido.mvnrepo.indexer.core.download;

public interface Consumer{
    public void notify(DownloadLink link, String content);
    public void error(Throwable e);
}