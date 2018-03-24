package frido.mvnrepo.indexer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JerseyHttpClient implements HttpClient {

    Logger log = LoggerFactory.getLogger(JerseyHttpClient.class);

    private String user;
    private String pwd;

    JerseyHttpClient() {

    }

    JerseyHttpClient(String user, String pwd) {
        this.user = user;
        this.pwd = pwd;
    }

    @Override
    public String get(String url) {
        log.trace("download: {}", url);
        Client client = Client.create(); //TODO: dont create cown client for every Task
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.get(ClientResponse.class);

        if (response.getStatus() != 200) {
            log.error("Failed : HTTP error code : " + response.getStatus() + ", url:" + url);
        }

        String output = response.getEntity(String.class);
        return output;
    }

    // https://api.github.com/graphql
    @Override
    public String post(String url, String query) throws Exception {
        log.debug("download: {}-{}", user, pwd);
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(this.user, this.pwd));
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.post(ClientResponse.class, query);
        if (response.getStatus() != 200) {
            throw new Exception(
                    "Failed : HTTP error code : " + response.getStatus() + " -> " + response.getEntity(String.class));
        }
        String output = response.getEntity(String.class);
        return output;
    }
}