package frido.mvnrepo.indexer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void notify(String content) {
        try{
        PomToJson converter = new PomToJson();
        Document doc = converter.toJsonMain(content);
        Document query = new Document(); // TODO: get query id from Document like Project
        query
            .append("ArtifactId", doc.getString("ArtifactId"))
            .append("GroupId", doc.getString("GroupId"))
            .append("Version", doc.getString("Version"));
        this.db.update("pom", query, doc);
        } finally {
            terminate();
        }
    }

    @Override
    public void error(Throwable e){
        terminate();
    }

    private void terminate(){
        int count = counter.incrementAndGet();
        if(count % 10 == 0) {
            log.debug("count: {} / {}", count, this.size);
        }
        if(count == this.size){
            this.executor.shutdown();
        }
    }

}