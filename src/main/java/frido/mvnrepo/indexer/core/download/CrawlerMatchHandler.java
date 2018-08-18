package frido.mvnrepo.indexer.core.download;

public interface CrawlerMatchHandler {
    public void match(DownloadLink link, String content);
}