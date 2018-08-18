package frido.mvnrepo.indexer.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadLink;

public class DummyPomHandler implements Consumer {

    Logger log = LoggerFactory.getLogger(DummyPomHandler.class);

    private String content;

    @Override
    public void notify(DownloadLink url, String content) {
        this.content = content;
    }

    @Override
    public void error(Throwable e){
        
    }

    public String getContent(){
        return this.content;
    }
}