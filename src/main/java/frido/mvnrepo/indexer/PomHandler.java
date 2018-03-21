package frido.mvnrepo.indexer;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PomHandler implements Consumer {

    Logger log = LoggerFactory.getLogger(PomHandler.class);
    private Database db;

    PomHandler(Database database) {
        this.db = database;
    }

    @Override
    public void notify(String content) {
        PomToJson converter = new PomToJson();
        Document pomJson = converter.toJsonMain(content);
        this.db.save("pom", pomJson);
    }

}