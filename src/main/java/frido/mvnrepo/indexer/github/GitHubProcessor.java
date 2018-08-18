package frido.mvnrepo.indexer.github;

import org.bson.Document;

import frido.mvnrepo.indexer.core.client.Client;
import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLink;
import frido.mvnrepo.indexer.core.download.Downloader;

public class GitHubProcessor {
    private Database db;
    private DownloadExecutor executor;

    public GitHubProcessor(Database database, DownloadExecutor executor){
        this.db = database;
        this.executor = executor;
    }

	public void start() {
        Iterable<Document> list = db.getGitHubRelated();
        Consumer consumer = new GitHubHandler(db);
        Client httpClient = new GitHubClient(new JerseyHttpClient("frido", System.getenv().get("GITHUB_KEY")));
        Downloader loader = new Downloader(executor, httpClient);
        list.forEach(doc -> loader.start(new DownloadLink(doc.getString("Url")), consumer) );
    }
}