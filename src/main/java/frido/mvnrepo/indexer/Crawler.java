package frido.mvnrepo.indexer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Crawler {

    private Executor executor;

    Crawler(){
        this.executor = Executors.newFixedThreadPool(50);
    }

    /**
     * Process link. Download, parse and call crawler for next steps
     */
    public void search(String link){
        Task task = new Task(this, link);
        this.executor.execute(task);
    }

    public void metadata(String link){
        System.out.println(Parser.parseMetadata(download(link)));
    }

    /**
     * Use http client to download url content
     */
    // FIXME: duplicated code from Task class
    private String download(String url){
        Client client = Client.create(); //TODO: dont create cown client for every Task
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus() + ", url:" + url);
        }

        String output = response.getEntity(String.class);
        return output;
    }

    
}