package frido.mvnrepo.indexer.core.download;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadLink implements Link {

	Logger log = LoggerFactory.getLogger(DownloadLink.class);

	private String link;

	public DownloadLink(String link) {
		this.link = link;
	}

	public boolean match(String filter) {
		return link.endsWith(filter);
	}

	public boolean isDirectory() {
		return link.endsWith("/") && !link.endsWith("../");
	}

	public DownloadLink append(String appendLink) {
		if (appendLink.startsWith("https://") || appendLink.startsWith("http://")) {
			return new DownloadLink(appendLink);
		}
		return new DownloadLink(link + appendLink);
	}

	@Override
	public String toString() {
		return this.link;
	}

	@Override
	public URI getURI() throws URISyntaxException {
		return new URI(link);
	}
}