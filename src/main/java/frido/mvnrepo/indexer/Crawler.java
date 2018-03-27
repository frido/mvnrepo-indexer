package frido.mvnrepo.indexer;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Crawler implements Consumer {

    Logger log = LoggerFactory.getLogger(Crawler.class);

    private static String PATTERN = "<a href=\"(.*?)\"";
    private static Pattern p = Pattern.compile(PATTERN);

    private CrawlerMatchHandler matchHandler;
    private String pattern;
    Downloader downloader;

    public Crawler(Downloader downloader, String match, CrawlerMatchHandler matchHandler) {

        this.matchHandler = matchHandler;
        this.pattern = match;
        this.downloader = downloader;
    }

    public void search(String link) {
        log.trace("search: {}", link);
        downloader.start(link, this);
    }

    public void match(String link) {
        log.trace("match: {}", link);
        this.downloader.start(link, new Consumer() {

            @Override
            public void notify(String url, String content) {
                matchHandler.match(url, content);
            }

            @Override
            public void error(Throwable e) {
                log.error("Crawler - Task - Error", e);
            }
        });
    }

    @Override
    public void notify(String url, String content) {
        List<String> links = getLinks(url, content);
        for (String link : links) {
            doNext(link);
        }
    }

    @Override
    public void error(Throwable e) {
        log.error("Crawler - Task - Error", e);
    }

    private List<String> getLinks(String url, String content) {
        log.trace("getLinks: {}", content);
        List<String> links = new LinkedList<String>();
        Matcher m = p.matcher(content);
        while (m.find()) {
            String link = getFullUrl(url, m.group(1));
            links.add(link);
        }
        return links;
    }

    // TODO: join with isValidLink
    private void doNext(String link) {
        log.trace("doNext: {}", link);
        if (isValidLink(link)) {
            if (link.endsWith(this.pattern)) {
                this.match(link);
            }
            if (link.endsWith("/")) {
                this.search(link);
            }
        }
    }

    private boolean isValidLink(String link) {
        log.trace("isValidLink(%s)", link);
        if (link.endsWith("../")) {
            return false;
        }
        if (link.endsWith(this.pattern) || link.endsWith("/")) {
            return true;
        }
        return false;
    }

    private String getFullUrl(String url, String link) {
        log.trace("getFullUrl: {}, {})", url, link);
        if (link.startsWith("https://") || link.startsWith("http://")) {
            return link;
        }
        return url + link;
    }

}