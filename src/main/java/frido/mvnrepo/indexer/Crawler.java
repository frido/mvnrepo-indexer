package frido.mvnrepo.indexer;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Crawler {

    Logger log = LoggerFactory.getLogger(Crawler.class);

    private Executor executor;
    private CrawlerMatchHandler matchHandler;
    private String pattern;
    private HttpClient httpClient;

    // TODO: Crawler can use Downloader
    public Crawler(String match, CrawlerMatchHandler matchHandler, Executor executor, HttpClient httpClient) {
        this.executor = executor;
        this.matchHandler = matchHandler;
        this.pattern = match;
        this.httpClient = httpClient;
    }

    /**
     * Process link. Download, parse and call crawler for next steps
     */
    public void search(String link) {
        log.trace("search: {}", link);
        this.executor.execute(new Task(this, link));
    }

    /**
     * Is called when link match pattern
     */
    public void match(String link) {
        log.trace("match: {}", link);
        matchHandler.match(link, download(link));
    }

    /**
     * Use http client to download url content.
     */    
    public String download(String link) {
        log.trace("download: {}", link);
        return httpClient.get(link);
    }

    /**
     * Return pattern to search for
     */
    public String getPattern(){
        return this.pattern;
    }

}