package frido.mvnrepo.indexer.core.download;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Crawler implements Consumer {

    Logger log = LoggerFactory.getLogger(Crawler.class);

    private static String LINK_PATTERN = "<a href=\"(.*?)\"";
    private static Pattern p = Pattern.compile(LINK_PATTERN);

    private CrawlerMatchHandler matchHandler;
    private String filter;
    Downloader downloader;

    public Crawler(Downloader downloader, String match, CrawlerMatchHandler matchHandler) {
        this.matchHandler = matchHandler;
        this.filter = match;
        this.downloader = downloader;
    }

    public void search(DownloadLink link) {
        log.trace("search: {}", link);
        downloader.start(link, this);
    }

    public void match(DownloadLink link) {
        log.trace("match: {}", link);
        this.downloader.start(link, new Consumer() {

            @Override
            public void notify(DownloadLink url, String content) {
                matchHandler.match(url, content);
            }

            @Override
            public void error(Throwable e) {
                log.error("Crawler - Task - Error", e);
            }
        });
    }

    @Override
    public void notify(DownloadLink link, String content) {
        List<DownloadLink> links = getLinks(link, content);
        for (DownloadLink item : links) {
            doNext(item);
        }
    }

    @Override
    public void error(Throwable e) {
        log.error("Crawler - Task - Error", e);
    }

    private List<DownloadLink> getLinks(DownloadLink link, String content) {
        log.trace("getLinks - content: {}", content);
        log.trace("getLinks - link: {}", link);
        List<DownloadLink> links = new LinkedList<>();
        Matcher m = p.matcher(content);
        while (m.find()) {
            links.add(link.append(m.group(1)));
            log.trace("getLinks - appender: {}", link.append(m.group(1)));
        }
        return links;
    }

    private void doNext(DownloadLink link) {
        log.trace("doNext: {}", link);
        if (link.match(this.filter)) {
            this.match(link);
        } else if (link.isDirectory()) {
            this.search(link);
        }
    }
}