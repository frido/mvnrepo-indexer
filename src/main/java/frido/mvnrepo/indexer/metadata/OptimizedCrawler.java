package frido.mvnrepo.indexer.metadata;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.download.Crawler;
import frido.mvnrepo.indexer.core.download.DownloadClient;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.Link;
import frido.mvnrepo.indexer.data.Metadata;

public class OptimizedCrawler extends Crawler {

	private Logger log = LoggerFactory.getLogger(OptimizedCrawler.class);

	private MetadataProcessor processor;

	public OptimizedCrawler(DownloadClient client, DownloadExecutor executor, MetadataProcessor processor) {
		super(client, executor, processor);
		this.processor = processor;
	}

	@Override
	public void accept(Link link, String content) {
		// log.trace("accept-link: " + link);
		List<Link> links = getLinks(link, content);
		Optional<Link> founded = matchByFilter(links);
		Optional<Metadata> foundedMetadata = validMetadata(founded);
		if (foundedMetadata.isPresent() && foundedMetadata.get().isValid(founded.get().toString())) {
			log.trace("accept-foundedMetadata: " + foundedMetadata.get().getIdentifier());
			processor.match(foundedMetadata.get());
		} else {
			for (Link item : links) {
				if (item.isDirectory()) {
					// log.trace("accept-download: " + item);
					this.download(item);
				}
			}
		}
	}

	private Optional<Metadata> validMetadata(Optional<Link> founded) {
		return founded.map(link -> {
			try {
				return this.manager.downloadSync(link);
			} catch (Exception e) {
				log.error("", e);
			}
			return null;
		}).map(content -> {
			try {
				return Metadata.valueOf(content, "");
			} catch (Exception e) {
				log.error("", e);
			}
			return null;
		});
	}

	private Optional<Link> matchByFilter(List<Link> links) {
		return links.stream().filter(l -> l.match(this.filter)).findFirst();
	}

}
