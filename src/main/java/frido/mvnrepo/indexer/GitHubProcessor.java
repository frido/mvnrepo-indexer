package frido.mvnrepo.indexer;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bson.Document;

public class GitHubProcessor {
    Database db;

    public GitHubProcessor(Database database){
        this.db = database;
    }

    public void start() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Database database = new MongoDatabase();
        Iterable<Document> list = database.getGitHubRelated();
        int size = 0;
        Iterator<Document> iterator = list.iterator();
        while(iterator.hasNext()){
            iterator.next();
            size++;
        }
        Consumer consumer = new GitHubHandler(database, executor, size);
        Client httpClient = new GitHubClient(new JerseyHttpClient("frido", System.getenv().get("GITHUB_KEY")));
        Downloader loader = new Downloader(executor, httpClient);
        list.forEach(doc -> loader.start(doc.getString("Url"), consumer) );
    }
}