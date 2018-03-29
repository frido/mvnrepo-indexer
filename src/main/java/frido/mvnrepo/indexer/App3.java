package frido.mvnrepo.indexer;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App3 {

    Logger log = LoggerFactory.getLogger(App3.class);

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Database database = new MongoDatabase();
        Collection<Document> list = database.getGitHubRelated();
        Consumer consumer = new GitHubHandler(database, executor, list.size());
        Client httpClient = new GitHubClient(new JerseyHttpClient("frido", System.getenv().get("GITHUB_KEY")));
        Downloader loader = new Downloader(executor, httpClient);
        list.forEach(doc -> loader.start(doc.getString("Url"), consumer) );
    }
}
