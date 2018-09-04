package frido.mvnrepo.indexer.pom;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadClient;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLink;
import frido.mvnrepo.indexer.core.download.DownloadManager;
import frido.mvnrepo.indexer.core.download.Link;
import frido.mvnrepo.indexer.data.Metadata;
import frido.mvnrepo.indexer.data.Pom;
import frido.mvnrepo.indexer.metadata.MetadataProcessor;

public class PomProcessor implements Consumer {

	Logger log = LoggerFactory.getLogger(MetadataProcessor.class);

	private Database db;
	private DownloadClient client;
	private DownloadExecutor executor;

	public PomProcessor(Database database, DownloadClient client, DownloadExecutor executorService) {
		this.db = database;
		this.client = client;
		this.executor = executorService;
	}

	public void start() {
		DownloadManager processor = new DownloadManager(client, executor);
		for (Document doc : this.db.getAll("metadata")) {
			try {
				Metadata metadata = Metadata.valueOf(doc);
				String pomLink = new PomLinkBuilder().repo(metadata.getRepo()).group(metadata.getGroup())
						.artifact(metadata.getArtifact()).version(metadata.getVersion()).build();
				processor.download(new DownloadLink(pomLink), this);
			} catch (PomUrlException e) {
				log.error("", e);
			}
		}
	}

	@Override
	public void accept(Link link, String content) {
		Pom pom = Pom.valueOf(content);
		this.db.update("pom", pom.getUniqFilter(), pom.getDocument());
	}

	@Override
	public void error(Exception e) {
		log.error("", e);

	}
}