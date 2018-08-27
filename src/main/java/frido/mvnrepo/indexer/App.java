package frido.mvnrepo.indexer;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.client.Client;
import frido.mvnrepo.indexer.core.client.DownloadClient;
import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.db.MongoDatabase;
import frido.mvnrepo.indexer.core.download.ComparableExecutor;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.Downloader;
import frido.mvnrepo.indexer.core.download.MetadataProvider;
import frido.mvnrepo.indexer.core.download.Provider;
import frido.mvnrepo.indexer.github.GitHubProcessor;
import frido.mvnrepo.indexer.pom.PomConsumer;
import frido.mvnrepo.indexer.pom.PomProcessor;

public class App {

	private static Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		Database database = new MongoDatabase();
		DownloadExecutor executor = new DownloadExecutor(new ComparableExecutor(5));

		CmdParser cmd = new CmdParser();
		try {
			cmd.parse(args);
		} catch (ParseException e) {
			log.error(e.getMessage());
		}

		if (cmd.metadataFlag()) {
			// MetadataHandler handler = new MetadataHandler(database, executor);
			// MetadataProcessor process1 = new MetadataProcessor(handler);
			// process1.start("http://central.maven.org/maven2/");
		}
		if (cmd.pomFlag()) {
			Provider provider = new MetadataProvider(database);
			Consumer consumer = new PomConsumer(database);
			Client httpClient = new DownloadClient(new JerseyHttpClient());
			Downloader loader = new Downloader(executor, httpClient);
			PomProcessor process2 = new PomProcessor(provider, loader, consumer);
			process2.start();
		}
		if (cmd.githubFlag()) {
			GitHubProcessor process3 = new GitHubProcessor(database, executor);
			process3.start();
		}
	}
}
