package frido.mvnrepo.indexer;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Task implements Runnable {

    private static String PATTERN = "<a href=\"(.*?)\"";
    private static Pattern p = Pattern.compile(PATTERN);

    private Crawler ctx;
    private String url;

    Task(Crawler context, String link) {
        this.ctx = context;
        this.url = link;
    }

    /**
     * Download url page.
     * Collect links.
     * Decide what to do next with link
     */
    @Override
    public void run() {
        String content = download(this.url);
        List<String> links = getLinks(content);
        for (String link : links) {
            doNext(link);
        }
    }

    /**
     * Use http client to download url content
     */
    private String download(String url) {
        Client client = Client.create(); //TODO: dont create cown client for every Task
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() + ", url:" + url);
        }

        String output = response.getEntity(String.class);
        return output;
    }

    /**
     * Search for links in text
     */
    private List<String> getLinks(String text) {
        List<String> links = new LinkedList<String>();
        Matcher m = p.matcher(text);
        while (m.find()) {
            String link = getFullUrl(this.url, m.group(1));
            links.add(link);
        }
        return links;
    }

    private void doNext(String link) {
        if (isValidLink(link)) {
            if (link.endsWith("maven-metadata.xml")) {
                this.ctx.metadata(link);
            }
            if (url.endsWith("/")) {
                this.ctx.search(link);
            }
        }
    }

    public boolean isValidLink(String link) {
        if (link.endsWith(".html") || link.endsWith(".xhtml") || link.endsWith(".pom") || link.endsWith("../")
                || link.endsWith(".jar") || link.endsWith(".gz") || link.endsWith(".zip") || link.endsWith(".asc")
                || link.endsWith(".md5") || link.endsWith(".sha1")) {
            return false;
        }
        return true;
    }

    /**
     * join base url and link to full url path
     */
    private String getFullUrl(String url, String link) {
        if (link.startsWith("https://") || link.startsWith("http://")) {
            return link;
        }
        return url + link;
    }
}