package frido.mvnrepo.indexer;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Downloader{

    Logger log = LoggerFactory.getLogger(Downloader.class);

    private Executor executor;
    private Mongo mongo;

    //TODO: duplicated from App
    private Map<String, String> getProperties(){
        Map<String, String> env = System.getenv();
        return env;
    }

    public Downloader(){
        this.executor = Executors.newFixedThreadPool(2);
        String connectionString = getProperties().get(App.ENV_MONGO_URL);
        this.mongo = new Mongo(connectionString); // TODO: there should be only one Mongo for all application
    }

    public void start(Document metadata){
        this.executor.execute(new DownloadTask(metadata, this));
    }

    public void notify(Document metadata, String projectUrl){
        Document newOne = new Document();
        newOne.put("groupId", metadata.getString("groupId"));
        newOne.put("artifactId", metadata.getString("artifactId"));
        newOne.put("version", metadata.getString("version"));
        newOne.put("projectUrl", projectUrl);
        log.info(".........: " + projectUrl + " -> " + newOne);
        this.mongo.update("metadata", metadata, newOne);
    }
}