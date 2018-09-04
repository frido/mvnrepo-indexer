package frido.mvnrepo.indexer.core.client;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class JerseyHttpClient implements HttpClient {

	Logger log = LoggerFactory.getLogger(JerseyHttpClient.class);

	Client client;

	public JerseyHttpClient() {
		this.client = Client.create();
	}

	public JerseyHttpClient(String user, String pwd) {
		this();
		client.addFilter(new HTTPBasicAuthFilter(user, pwd));
	}

	@Override
	public String get(URI url) throws ClientException {
		log.trace("download: {}", url);
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new ClientException(url.toString(), null, response.getStatus(), response.getEntity(String.class));
		}
		return response.getEntity(String.class);
	}

	@Override
	public String post(URI uri, String query) {
		log.trace("download: {}-{}", uri.toString(), query);
		WebResource webResource = client.resource(uri);
		ClientResponse response = webResource.post(ClientResponse.class, query);
		if (response.getStatus() != 200) {
			try {
				throw new ClientException(uri.toString(), query, response.getStatus(),
						response.getEntity(String.class));
			} catch (ClientHandlerException | UniformInterfaceException | ClientException e) {
				e.printStackTrace();
			}
		}
		return response.getEntity(String.class);
	}
}