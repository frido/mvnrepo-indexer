package frido.mvnrepo.indexer;

import java.io.IOException;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.db.Datasource;
import frido.mvnrepo.indexer.core.db.FileDatasource;
import frido.mvnrepo.indexer.core.db.MongoDatabase;
import frido.mvnrepo.indexer.core.db.Repository;
import frido.mvnrepo.indexer.core.download.ComparableExecutor;
import frido.mvnrepo.indexer.core.download.DownloadClient;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLinkClient;
import frido.mvnrepo.indexer.data.Metadata;
import frido.mvnrepo.indexer.metadata.MetadataConverter;
import frido.mvnrepo.indexer.metadata.MetadataProcessor;
import frido.mvnrepo.indexer.metadata.MetadataRepository;
import frido.mvnrepo.indexer.pom.PomProcessor;
import frido.mvnrepo.indexer.stats.StatProcessor;

public class App {

	private static Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws IOException {

		CmdParser cmd = new CmdParser();
		try {
			cmd.parse(args);
		} catch (ParseException e) {
			cmd.printHelp();
			e.printStackTrace();
		}

		if (args.length == 0) {
			cmd.printHelp();
		}

		if (cmd.metadataFlag()) {
			System.out.println("metadata");
			System.out.println("repository: " + cmd.repository());
			System.out.println("threads: " + cmd.threadsCount());
			Repository<Metadata> repository;
			try (Datasource datasource = new FileDatasource()) {
				DownloadExecutor executor = new DownloadExecutor(
						new ComparableExecutor(Integer.parseInt(cmd.threadsCount())));
				DownloadClient client = new DownloadLinkClient(new JerseyHttpClient());
				MetadataProcessor processor = new MetadataProcessor(datasource, client, executor);
				processor.start(cmd.repository());
				executor.waitForTerminate();
				repository = new MetadataRepository(datasource, new MetadataConverter());
			}
			Iterable<Metadata> list = repository.getAll();
			int count = 0;
			for (Metadata i : list) {
				count++;
			}
			System.out.println("downloaded: " + count);
		}
		if (cmd.pomFlag()) {
			System.out.println("pom");
			System.out.println("threads: " + cmd.threadsCount());
			try (Datasource fileDatasource = new FileDatasource(); Datasource mongoDatasource = new MongoDatabase()) {
				DownloadExecutor executor = new DownloadExecutor(
						new ComparableExecutor(Integer.parseInt(cmd.threadsCount())));
				DownloadClient client = new DownloadLinkClient(new JerseyHttpClient());
				PomProcessor processor = new PomProcessor(fileDatasource, mongoDatasource, client, executor);
				processor.start();
				executor.waitForTerminate();
			}
		}

		if (cmd.githubFlag()) {
			System.out.println("github");
		}

		if (cmd.statisticsFlag()) {
			try (Datasource datasource = new FileDatasource()) {
				StatProcessor stat = new StatProcessor(datasource);
				stat.start();
			}

		}

//		
//
//		datasource.close();

//		Iterable<Metadata> list = repository.getAll();
//		int count = 0;
//		for (Metadata i : list) {
//			count++;
//		}
//
//		System.out.println(count);
//
//		CmdParser cmd = new CmdParser();
//		try {
//			cmd.parse(args);
//		} catch (ParseException e) {
//			log.error(e.getMessage());
//		}

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
