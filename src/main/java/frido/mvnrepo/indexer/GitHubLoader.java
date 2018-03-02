package frido.mvnrepo.indexer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubLoader{

    Logger log = LoggerFactory.getLogger(GitHubLoader.class);

    private Executor executor;
    private Mongo mongo;

    private Set<String> projects;

    //TODO: duplicated from App
    private Map<String, String> getProperties(){
        Map<String, String> env = System.getenv();
        return env;
    }

    public GitHubLoader(){
        this.executor = Executors.newFixedThreadPool(2);
        String connectionString = getProperties().get(App.ENV_MONGO_URL);
        this.mongo = new Mongo(connectionString);
        this.projects = Collections.synchronizedSet(new HashSet<String>());
    }

    public void start(String gitHubLink){
        if(this.projects.add(gitHubLink)){
            //Document github = new Document("url", gitHubLink);
            this.executor.execute(new GitHubLoaderTask(gitHubLink, this));
        } 
        
    }

    public void notify(String github, String payload){
        log.info(".........: " + github + " -> " + payload);
    }
}