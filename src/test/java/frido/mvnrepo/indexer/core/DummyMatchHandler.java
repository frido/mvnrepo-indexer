package frido.mvnrepo.indexer.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.download.CrawlerMatchHandler;

public class DummyMatchHandler implements CrawlerMatchHandler {

    Logger log = LoggerFactory.getLogger(DummyMatchHandler.class);

    private int counter = 0;

	@Override
	public void match(String link, String content) {
        log.info("match: {}", link);
        counter++;
    }
    
    public int getCount(){
        return this.counter;
    }

}