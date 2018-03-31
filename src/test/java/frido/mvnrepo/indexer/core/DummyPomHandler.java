package frido.mvnrepo.indexer.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.download.Consumer;

public class DummyPomHandler implements Consumer {

    Logger log = LoggerFactory.getLogger(DummyPomHandler.class);

    private String content;

    @Override
    public void notify(String url, String content) {
        this.content = content;
    }

    @Override
    public void error(Throwable e){
        
    }

    public String getContent(){
        return this.content;
    }
}