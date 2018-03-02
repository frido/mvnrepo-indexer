package frido.mvnrepo.indexer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadTask implements Runnable {

    Logger log = LoggerFactory.getLogger(DownloadTask.class);

    private Document metadata;
    private Downloader ctx;

    public DownloadTask(Document metadata, Downloader ctx) {
        this.metadata = metadata;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        try {
            String link = generateLink(this.metadata);
            String content = download(link);
            PomToJson converter = new PomToJson();
            Document json = converter.toJsonMain(content); // TODO: better function name7
            String url = json.getString("Url"); //TODO: Optional
            if (url != null) {
                this.ctx.notify(this.metadata, url);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private String generateLink(Document doc) throws Exception { //TODO: input groupId, arch... instead of Document
        String groupId = doc.getString("groupId");
        String artifactId = doc.getString("artifactId");
        String version = doc.getString("version");
        if (groupId == null || artifactId == null || version == null) {
            throw new Exception("Wrong metadata: " + doc);
        }
        return "http://central.maven.org/maven2/" + groupId.replace(".", "/") + "/" + artifactId + "/" + version + "/"
                + artifactId + "-" + version + ".pom";
    }

    //TODO: duplicitny kod z crawler-a
    private String download(String url) throws Exception {
        log.debug("download({})", url);
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.get(ClientResponse.class);

        if (response.getStatus() != 200) {
            //throw new RuntimeException(
            //   "Failed : HTTP error code : " + response.getStatus() + ", url:" + url);
            throw new Exception("Failed : HTTP error code : " + response.getStatus() + ", url:" + url);
        }

        String output = response.getEntity(String.class);
        return output;
    }

}