package frido.mvnrepo.indexer.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.db.Datasource;
import frido.mvnrepo.indexer.core.db.Repository;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadClient;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLink;
import frido.mvnrepo.indexer.core.download.Link;
import frido.mvnrepo.indexer.data.Metadata;

public class MetadataProcessor implements Consumer {

	Logger log = LoggerFactory.getLogger(MetadataProcessor.class);

	private Repository<Metadata> db;
	private DownloadClient client;
	private DownloadExecutor executor;
	private String repo;

	public MetadataProcessor(Datasource datasource, DownloadClient client, DownloadExecutor executorService) {
		this.db = new MetadataRepository(datasource, new MetadataConverter());
		this.client = client;
		this.executor = executorService;
	}

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
		db.update(metadata);
	}

}
