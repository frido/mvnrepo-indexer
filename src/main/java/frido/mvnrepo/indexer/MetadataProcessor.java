package frido.mvnrepo.indexer;

import java.util.concurrent.ExecutorService;

public class MetadataProcessor {
    Database db;

    public MetadataProcessor(Database database){
        this.db = database;
    }

    public void start() {
        CrawlerMatchHandler handler = new StoreMatchHandler(db);
        ExecutorService executor =  new ComparableExecutor(5);
        UrlClient httpClient = new UrlClient(new JerseyHttpClient());
        Downloader downloader = new Downloader(executor, httpClient);
        Crawler c2 = new Crawler(downloader, "maven-metadata.xml", handler);
        c2.search("http://central.maven.org/maven2/");
    }
}