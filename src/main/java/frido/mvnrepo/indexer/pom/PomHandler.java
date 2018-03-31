package frido.mvnrepo.indexer.pom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.Consumer;

public class PomHandler implements Consumer {

    Logger log = LoggerFactory.getLogger(PomHandler.class);
    private Database db;
    private ExecutorService executor;
    private int size;
    private AtomicInteger counter = new AtomicInteger(0);

    PomHandler(Database database, ExecutorService executor, int size) {
        this.db = database;
        this.executor = executor;
        this.size = size;
    }

    @Override
    public void notify(String url, String xml) {
        try {
            Pom pom = Pom.valueOf(xml);
            this.db.update("pom", pom.getUniqFilter(), pom.getDocument());
        } finally {
            terminate();
        }
    }

    @Override
    public void error(Throwable e) {
        terminate();
    }

    private void terminate() {
        int count = counter.incrementAndGet();
        if (count % 10 == 0) {
            log.debug("count: {} / {}", count, this.size);
        }
        if (count == this.size) {
            this.executor.shutdown();
        }
    }

}