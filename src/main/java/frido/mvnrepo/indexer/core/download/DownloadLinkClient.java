package frido.mvnrepo.indexer.core.download;

import java.net.URISyntaxException;

import frido.mvnrepo.indexer.core.client.ClientException;
import frido.mvnrepo.indexer.core.client.HttpClient;

public class DownloadLinkClient implements DownloadClient {

	private HttpClient client;

	public DownloadLinkClient(HttpClient client) {
		this.client = client;
	}

	@Override
	public String download(Link link) throws ClientException, URISyntaxException {
		return client.get(link.getURI());
	}

}
