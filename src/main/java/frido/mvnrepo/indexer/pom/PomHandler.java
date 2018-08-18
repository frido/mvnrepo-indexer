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

    PomHandler(Database database) {
        this.db = database;
    }

    @Override
    public void notify(String url, String xml) {
            Pom pom = Pom.valueOf(xml);
            this.db.update("pom", pom.getUniqFilter(), pom.getDocument());
    }

    @Override
    public void error(Throwable e) {
    }
}