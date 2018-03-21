package frido.mvnrepo.indexer;

public interface CrawlerMatchHandler {
    public void match(String link, String content);
}