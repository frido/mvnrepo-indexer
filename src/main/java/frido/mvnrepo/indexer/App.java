package frido.mvnrepo.indexer;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        Database database = new MongoDatabase();
        CrawlerMatchHandler handler = new StoreMatchHandler(database);
        Executor executor =  new ComparableExecutor(5);
        //Executor executor =  Executors.newFixedThreadPool(5);
        UrlClient httpClient = new UrlClient(new JerseyHttpClient());
        Downloader downloader = new Downloader(executor, httpClient);
        Crawler c2 = new Crawler(downloader, "maven-metadata.xml", handler);
        c2.search("http://central.maven.org/maven2/");
        // Database database = new MongoDatabase();
        // CrawlerMatchHandler handler = new StoreMatchHandler(database);
        // //Executor executor = new NoThreadExecutor();
        // Executor executor = new ComparableExecutor(5);
        // HttpClient httpClient = new JerseyHttpClient();
        // Crawler crawler = new Crawler("maven-metadata.xml", handler, executor, httpClient);
        // crawler.search("http://central.maven.org/maven2/");
    }
}
