package frido.mvnrepo.indexer.github;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bson.Document;

import frido.mvnrepo.indexer.core.client.Client;
import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.db.MongoDatabase;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.Downloader;

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