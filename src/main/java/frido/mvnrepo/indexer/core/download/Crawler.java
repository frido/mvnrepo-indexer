package frido.mvnrepo.indexer.core.download;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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

    AtomicInteger counter = new AtomicInteger(0);

    public Crawler(Downloader downloader, String match, CrawlerMatchHandler matchHandler) {

        this.matchHandler = matchHandler;
        this.filter = match;
        this.downloader = downloader;
    }

    public void search(String link) {
        log.trace("search: {}", link);
        System.out.println("+ "+link+".........................");
        counter.incrementAndGet();
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
        System.out.println("- "+url+".........................");
        counter.decrementAndGet();
        isLast();
    }

    private void isLast() {
        if(counter.get() == 0){
            System.out.println("KONIEC.........................");
            matchHandler.terminate();
        }
        System.out.println(counter.get()+".........................");
	}

	@Override
    public void error(Throwable e) {
        log.error("Crawler - Task - Error", e);
        System.out.println("+ "+"error"+".........................");
        counter.decrementAndGet();
        isLast();
    }

    private List<String> getLinks(String url, String content) {
        log.trace("getLinks: {}", content);
        List<String> links = new LinkedList<>();
        Matcher m = p.matcher(content);
        while (m.find()) {
            String link = getFullUrl(url, m.group(1));
            links.add(link);
        }
        return links;
    }

    private void doNext(String link) {
        log.trace("doNext: {}", link);
        if (link.endsWith(this.filter)) {
            this.match(link);
        }else if (link.endsWith("/") && !link.endsWith("../")) {
            this.search(link);
        }
    }

    private String getFullUrl(String url, String link) {
        log.trace("getFullUrl: {}, {})", url, link);
        if (link.startsWith("https://") || link.startsWith("http://")) {
            return link;
        }
        return url + link;
    }

}