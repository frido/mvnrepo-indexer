package frido.mvnrepo.indexer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubLoaderTask implements Runnable {

    Logger log = LoggerFactory.getLogger(GitHubLoaderTask.class);

    private String gitHubLink;
    private GitHubLoader ctx;

    public GitHubLoaderTask(String gitHubLink, GitHubLoader ctx) {
        this.gitHubLink = gitHubLink;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        try {
            String content = download("request", "request");
            this.ctx.notify(this.gitHubLink, content);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    //TODO: duplicitny kod z crawler-a
    private String download(String username, String repo) throws Exception {
        log.debug("download({}-{})", username, repo);

        //HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("username", "password");

        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("frido", "***"));
        WebResource webResource = client.resource("https://api.github.com/graphql");
        //webResource.header("Authorization", "Basic ZnJpZG86OTQ4MjhhOWI1YTI5MzAwY2RlZTExZjQ4NjEwYTgwNTJiNjdjMjJmMA==");
        //ClientResponse response = webResource.get(ClientResponse.class);
        ClientResponse response = webResource.post(ClientResponse.class, "{ \"query\": \"query Test($myVariable: String!) {repository(owner:$myVariable, name:$myVariable){stargazers {totalCount}watchers{totalCount}forks{totalCount}}}\",\"variables\": { \"myVariable\": \"request\" }}");

        if (response.getStatus() != 200) {
            throw new Exception("Failed : HTTP error code : " + response.getStatus() + " -> " + response.getEntity(String.class));
        }

        String output = response.getEntity(String.class);
        return output;
    }

}