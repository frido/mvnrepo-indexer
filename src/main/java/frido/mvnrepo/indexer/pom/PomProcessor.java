package frido.mvnrepo.indexer.pom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.artifact.Metadata;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadLink;
import frido.mvnrepo.indexer.core.download.Downloader;
import frido.mvnrepo.indexer.core.download.Provider;

public class PomProcessor {
	static Logger log = LoggerFactory.getLogger(PomProcessor.class);

	private Downloader loader;
	private Provider provider;
	private Consumer consumer;

	public PomProcessor(Provider provider, Downloader downloader, Consumer consumer) {
		this.loader = downloader;
		this.provider = provider;
		this.consumer = consumer;
	}

	public void start() {
		provider.provide().forEach(doc -> {
			String pomLink = null;
			try {
				pomLink = new Metadata(doc).getPomLink();
			} catch (PomUrlException e) {
				log.error("Artifact - pomLink", e);
				consumer.error(e);
				return;
			}
			loader.start(new DownloadLink(pomLink), consumer);
		});
	}
}