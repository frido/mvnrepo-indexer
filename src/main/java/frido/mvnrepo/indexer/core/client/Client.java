package frido.mvnrepo.indexer.core.client;

import frido.mvnrepo.indexer.core.download.DownloadLink;

public interface Client{
    public String download(DownloadLink link) throws Exception;
}