package frido.mvnrepo.indexer.pom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.db.Datasource;
import frido.mvnrepo.indexer.core.db.Repository;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadClient;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLink;
import frido.mvnrepo.indexer.core.download.DownloadManager;
import frido.mvnrepo.indexer.core.download.Link;
import frido.mvnrepo.indexer.data.Metadata;
import frido.mvnrepo.indexer.data.Pom;
import frido.mvnrepo.indexer.metadata.MetadataConverter;
import frido.mvnrepo.indexer.metadata.MetadataProcessor;
import frido.mvnrepo.indexer.metadata.MetadataRepository;

public class PomProcessor implements Consumer {

	Logger log = LoggerFactory.getLogger(MetadataProcessor.class);

	private Repository<Metadata> metadata;
	private Repository<Pom> pomLocal;
	private Repository<Pom> pomRemote;
	private DownloadClient client;
	private DownloadExecutor executor;

	public PomProcessor(Datasource local, Datasource remote, DownloadClient client, DownloadExecutor executorService) {
		this.metadata = new MetadataRepository(local, new MetadataConverter());
		this.pomLocal = new PomRepository(local, new PomConverter());
		this.pomRemote = new PomRepository(remote, new PomConverter());
		this.client = client;
		this.executor = executorService;
	}

	public void start() {
		DownloadManager processor = new DownloadManager(client, executor);
		for (Metadata metadata : this.metadata.getAll()) {
			try {
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
		String gitUrl = pom.getGitUrl();
		this.pomLocal.save(pom);
		if (isGitHub(gitUrl)) {
			this.pomRemote.update(pom);
		}
	}

	private boolean isGitHub(String link) {
		if (link != null && link.startsWith("https://github.com/")) {
			return true;
		}
		return false;
	}

	@Override
	public void error(Exception e) {
		log.error("", e);

	}
}