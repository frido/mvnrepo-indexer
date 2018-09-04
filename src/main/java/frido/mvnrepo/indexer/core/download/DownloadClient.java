package frido.mvnrepo.indexer.core.download;

import java.net.URISyntaxException;

import frido.mvnrepo.indexer.core.client.ClientException;

public interface DownloadClient {
	public String download(Link link) throws ClientException, URISyntaxException;
}
