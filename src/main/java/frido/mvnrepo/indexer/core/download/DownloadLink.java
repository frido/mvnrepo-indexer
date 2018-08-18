package frido.mvnrepo.indexer.core.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadLink {

    Logger log = LoggerFactory.getLogger(DownloadLink.class);
    
    private String link;
    
    public DownloadLink(String link){
        this.link = link;
    }

    public boolean match(String filter) {
        return link.endsWith(filter);
    }

    public boolean isDirectory() {
        return link.endsWith("/") && !link.endsWith("../");
    }

    public DownloadLink append(String appendLink) {
        if (appendLink.startsWith("https://") || appendLink.startsWith("http://")) {
            return new DownloadLink(appendLink);
        }
        return new DownloadLink(link + appendLink);
    }

    public int getDeep() {
        int deep = 0;
        for (char ch : link.toCharArray()) {
            if (ch == '/') {
                deep++;
            }
        }
        return deep;
    }

    public String getLink(){
        return link;
    }

    public String toString(){
        return this.link;
    }
}