package frido.mvnrepo.indexer.core.download;

public interface CrawlerMatchHandler {
    public void match(String link, String content);
    //public void terminate();
}