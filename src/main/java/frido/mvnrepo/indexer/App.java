package frido.mvnrepo.indexer;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.db.MongoDatabase;
import frido.mvnrepo.indexer.core.download.ComparableExecutor;
import frido.mvnrepo.indexer.core.download.DownloadClient;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLinkClient;
import frido.mvnrepo.indexer.metadata.MetadataProcessor;

public class App {

	private static Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		System.out.println("1");

		Database database = new MongoDatabase();
		DownloadExecutor executor = new DownloadExecutor(new ComparableExecutor(50));
		DownloadClient client = new DownloadLinkClient(new JerseyHttpClient());

		MetadataProcessor processor = new MetadataProcessor(database, client, executor);
		processor.start("http://central.maven.org/maven2/");

		System.out.println("2");

		CmdParser cmd = new CmdParser();
		try {
			cmd.parse(args);
		} catch (ParseException e) {
			log.error(e.getMessage());
		}

		if (cmd.metadataFlag()) {
//			MetadataProcessor processor = new MetadataProcessor(database, client, executor);
//			processor.start("http://central.maven.org/maven2/ant/");
		}
		if (cmd.pomFlag()) {
//			Provider provider = new MetadataProvider(database);
//			Consumer consumer = new PomConsumer(database);
//			Client httpClient = new DownloadClient(new JerseyHttpClient());
//			Downloader loader = new Downloader(executor, httpClient);
//			PomProcessor process2 = new PomProcessor(provider, loader, consumer);
//			process2.start();
		}
		if (cmd.githubFlag()) {
//			GitHubProcessor process3 = new GitHubProcessor(database, executor);
//			process3.start();
		}
	}
}
