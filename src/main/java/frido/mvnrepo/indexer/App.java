package frido.mvnrepo.indexer;

import java.io.FileNotFoundException;

import org.apache.commons.cli.ParseException;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.db.Converter;
import frido.mvnrepo.indexer.core.db.Datasource;
import frido.mvnrepo.indexer.core.db.FileDatasource;
import frido.mvnrepo.indexer.core.db.Repository;
import frido.mvnrepo.indexer.core.download.ComparableExecutor;
import frido.mvnrepo.indexer.core.download.DownloadClient;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLinkClient;
import frido.mvnrepo.indexer.data.Metadata;
import frido.mvnrepo.indexer.metadata.MetadataConverter;
import frido.mvnrepo.indexer.metadata.MetadataProcessor;
import frido.mvnrepo.indexer.metadata.MetadataRepository;

public class App {

	private static Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws FileNotFoundException {

		System.out.println("1");

		Datasource datasource = new FileDatasource("./matedata.db");
		Converter<Metadata, Document> converter = new MetadataConverter();
		Repository<Metadata> repository = new MetadataRepository(datasource, converter);
		DownloadExecutor executor = new DownloadExecutor(new ComparableExecutor(50));
		DownloadClient client = new DownloadLinkClient(new JerseyHttpClient());
		MetadataProcessor processor = new MetadataProcessor(repository, client, executor);
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
