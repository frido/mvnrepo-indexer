package frido.mvnrepo.indexer.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadClient;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLink;
import frido.mvnrepo.indexer.core.download.Link;
import frido.mvnrepo.indexer.data.Metadata;

public class MetadataProcessor implements Consumer {

	Logger log = LoggerFactory.getLogger(MetadataProcessor.class);

	private Database db;
	private DownloadClient client;
	private DownloadExecutor executor;
	private String repo;

	public MetadataProcessor(Database database, DownloadClient client, DownloadExecutor executorService) {
		this.db = database;
		this.client = client;
		this.executor = executorService;
	}

	// "http://central.maven.org/maven2/"
	public void start(String repo) {
		this.repo = repo;
		OptimizedCrawler crawler = new OptimizedCrawler(client, executor, this);
		crawler.search(new DownloadLink(repo), "maven-metadata.xml");
	}

	@Override
	public void accept(Link link, String content) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void error(Exception e) {
		log.error("", e);
	}

	public void match(Metadata metadata) {
		metadata.setRepo(repo);
		// log.trace("match: " + metadata.getIdentifierFilter());
		this.db.update("metadata", metadata.getIdentifierFilter(), metadata.getDocument()); // TODO: as part of
	}

}
