package frido.mvnrepo.indexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DummyMatchHandler implements CrawlerMatchHandler {

    Logger log = LoggerFactory.getLogger(DummyMatchHandler.class);

    private int counter = 0;

	@Override
	public void match(String link, String content) {
        log.trace("match: {}", content);
        counter++;
    }
    
    public int getCount(){
        return this.counter;
    }

}