package frido.mvnrepo.indexer.core.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class JerseyHttpClient implements HttpClient {

    Logger log = LoggerFactory.getLogger(JerseyHttpClient.class);

    Client client;

    public JerseyHttpClient() {
        this.client = Client.create();
    }

    public JerseyHttpClient(String user, String pwd)  {
        this();
        client.addFilter(new HTTPBasicAuthFilter(user, pwd));
    }

    @Override
    public String get(String url) throws ClientException {
        log.trace("download: {}", url);
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new ClientException(url, null, response.getStatus(), response.getEntity(String.class));
        }
        return response.getEntity(String.class);
    }

    @Override
    public String post(String url, String query) throws ClientException {
        log.trace("download: {}-{}", url, query);
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.post(ClientResponse.class, query);
        if (response.getStatus() != 200) {
            throw new ClientException(url, query, response.getStatus(), response.getEntity(String.class));
        }
        return response.getEntity(String.class);
    }
}