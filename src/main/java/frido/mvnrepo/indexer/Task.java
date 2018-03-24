package frido.mvnrepo.indexer;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Task implements Runnable, Prioritable {

    Logger log = LoggerFactory.getLogger(Task.class);

    private static String PATTERN = "<a href=\"(.*?)\"";
    private static Pattern p = Pattern.compile(PATTERN);
    private int priority = 0;

    private Crawler ctx;
    private String url;

    public Task(Crawler context, String link) {
        log.trace("Task: {}", link);
        this.ctx = context;
        this.url = link;
        this.priority = calculatePriority(link);
    }

    private int calculatePriority(String link) {
        int count = 0;
        for (char ch : link.toCharArray()) {
            if (ch == '/') {
                count++;
            }
        }
        return count;
    }

    public int getPriority() {
        return this.priority;
    }

    /**
     * Download url page.
     * Collect links.
     * Decide what to do next with link.
     */
    @Override
    public void run() {
        log.trace("run: {}", this.url);
        try {
            String content = this.ctx.download(this.url);
            List<String> links = getLinks(content);
            for (String link : links) {
                doNext(link);
            }
        } catch (Exception e) {
            log.error("Crawler - Task - Error", e);
        }
    }

    /**
     * Search for links in text.
     */
    private List<String> getLinks(String text) {
        log.trace("getLinks: {}", text);
        List<String> links = new LinkedList<String>();
        Matcher m = p.matcher(text);
        while (m.find()) {
            String link = getFullUrl(this.url, m.group(1));
            links.add(link);
        }
        return links;
    }

    /**
     * Process next step with finded links.
     */
    private void doNext(String link) {
        log.trace("doNext: {}", link);
        if (isValidLink(link)) {
            if (link.endsWith(this.ctx.getPattern())) {
                this.ctx.match(link);
            }
            if (link.endsWith("/")) {
                this.ctx.search(link);
            }
        }
    }

    /**
     * Check file extension in the link.
     */
    public boolean isValidLink(String link) {
        log.trace("isValidLink(%s)", link);
        if (link.endsWith("../")) {
            return false;
        }
        if (link.endsWith(this.ctx.getPattern()) || url.endsWith("/")) {
            return true;
        }
        return false;
    }

    /**
     * Join base url and link to full url path.
     */
    private String getFullUrl(String url, String link) {
        log.trace("getFullUrl: {}, {})", url, link);
        if (link.startsWith("https://") || link.startsWith("http://")) {
            return link;
        }
        return url + link;
    }
}