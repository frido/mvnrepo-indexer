package frido.mvnrepo.indexer;

import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Crawler {

    Logger log = LoggerFactory.getLogger(Crawler.class);

    private Executor executor;
    private MatchHandler matchHandler;

    Crawler(MatchHandler matchHandler) {
        this.executor = Executors.newFixedThreadPool(50);//TODO: configurable value 50
        //TODO: comparable priorty queue executor
        this.executor = new ThreadPoolExecutor(10, 50, 100, TimeUnit.HOURS, new PriorityBlockingQueue<Runnable>(100, new Comparator<Runnable>() {
        @Override
        public int compare(Runnable o1, Runnable o2) {
            Task t1 = (Task) o1;
            Task t2 = (Task) o2;
            return (-1) * (t1.getDeep() - t2.getDeep()); // reverse order
        }
        }));
        this.matchHandler = matchHandler;
    }

    /**
     * Process link. Download, parse and call crawler for next steps
     */
    public void search(String link, int deep) {
        log.debug("search({})", link);
        Task task = new Task(this, link, ++deep);
        this.executor.execute(task);
    }

    public void match(String link) {
        log.debug("match({})", link);
        matchHandler.match(download(link));
    }

    /**
     * Use http client to download url content.
     */    
    public String download(String url) { //TODO: alternative httpClient
        log.debug("download({})", url);
        Client client = Client.create(); //TODO: dont create cown client for every Task
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException(
                "Failed : HTTP error code : " + response.getStatus() + ", url:" + url);
        }

        String output = response.getEntity(String.class);
        return output;
    }

}