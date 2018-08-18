package frido.mvnrepo.indexer.github;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;

import org.bson.Document;

import frido.mvnrepo.indexer.core.client.Client;
import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.Downloader;
import frido.mvnrepo.indexer.core.download.MyExecutor;

public class GitHubProcessor {
    private Database db;
    private MyExecutor executor;

    public GitHubProcessor(Database database, MyExecutor executor){
        this.db = database;
        this.executor = executor;
    }

	public void start() {
        Iterable<Document> list = db.getGitHubRelated();
        int size = 0;
        Iterator<Document> iterator = list.iterator();
        while(iterator.hasNext()){
            iterator.next();
            size++;
        }
        Consumer consumer = new GitHubHandler(db, executor, size);
        Client httpClient = new GitHubClient(new JerseyHttpClient("frido", System.getenv().get("GITHUB_KEY")));
        Downloader loader = new Downloader(executor, httpClient);
        list.forEach(doc -> loader.start(doc.getString("Url"), consumer) );
    }
}