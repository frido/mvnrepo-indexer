package frido.mvnrepo.indexer.core.client;

import java.net.URI;

public interface HttpClient {
	public String get(URI url) throws ClientException;
	public String post(URI url, String body) throws ClientException;
}