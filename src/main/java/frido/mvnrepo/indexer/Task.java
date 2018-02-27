package frido.mvnrepo.indexer;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Task implements Runnable {

    Logger log = LoggerFactory.getLogger(Task.class);

    private static String PATTERN = "<a href=\"(.*?)\"";
    private static Pattern p = Pattern.compile(PATTERN);
    private int deep = 0;

    private Crawler ctx;
    private String url;

    Task(Crawler context, String link, int deep) {
        log.trace("Task({})", link);
        this.ctx = context;
        this.url = link;
        this.deep = deep;
    }

    public int getDeep(){
        return this.deep;
    }

    /**
     * Download url page.
     * Collect links.
     * Decide what to do next with link.
     */
    @Override
    public void run() {
        log.debug("run({})", this.url);
        String content = download(this.url);
        List<String> links = getLinks(content);
        for (String link : links) {
            doNext(link);
        }
    }

    /**
     * Use http client to download url content.
     */
    private String download(String link) {
        log.trace("download{})", link);
        return this.ctx.download(link);
    }

    /**
     * Search for links in text.
     */
    private List<String> getLinks(String text) {
        log.trace("getLinks(%s)", text);
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
        log.trace("doNext({}})", link);
        if (isValidLink(link)) {
            if (link.endsWith("maven-metadata.xml")) { //TODO: configurable search string
                this.ctx.match(link);
            }
            if (link.endsWith("/")) {
                this.ctx.search(link, deep);
            }
        }
    }

    /**
     * Check file extension in the link.
     */
    public boolean isValidLink(String link) {
        log.trace("isValidLink(%s)", link);
        // if (link.endsWith(".html") || link.endsWith(".xhtml") || link.endsWith(".pom") || link.endsWith("../")
        //         || link.endsWith(".jar") || link.endsWith(".gz") || link.endsWith(".zip") || link.endsWith(".asc")
        //         || link.endsWith(".md5") || link.endsWith(".sha1")) {
        //     return false;
        // }
        // return true;
        if (link.endsWith("../")) {
            return false;
        }
        if (link.endsWith("maven-metadata.xml") || url.endsWith("/")) { //TODO: configurable search string
            return true;
        } 
        return false;
    }

    /**
     * Join base url and link to full url path.
     */
    private String getFullUrl(String url, String link) {
        log.trace("getFullUrl(%s + %s)", url, link);
        if (link.startsWith("https://") || link.startsWith("http://")) {
            return link;
        }
        return url + link;
    }
}