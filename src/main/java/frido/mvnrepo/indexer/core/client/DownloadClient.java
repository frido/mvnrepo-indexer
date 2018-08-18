package frido.mvnrepo.indexer.core.client;

import frido.mvnrepo.indexer.core.download.DownloadLink;

public class DownloadClient implements Client {

    private HttpClient client;

    public DownloadClient(HttpClient client){
        this.client = client;
    }

    public String download(DownloadLink link) throws Exception {
        return this.client.get(link.getLink());
    }
}