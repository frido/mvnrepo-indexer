package frido.mvnrepo.indexer;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.util.JSON;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubLoader {

    Logger log = LoggerFactory.getLogger(GitHubLoader.class);

    private Executor executor;
    private Mongo mongo;

    private Set<String> projects;

    //TODO: duplicated from App
    private Map<String, String> getProperties() {
        Map<String, String> env = System.getenv();
        return env;
    }

    public GitHubLoader() {
        this.executor = Executors.newFixedThreadPool(5);
        String connectionString = getProperties().get(App.ENV_MONGO_URL);
        this.mongo = new Mongo(connectionString);
        this.projects = Collections.synchronizedSet(new HashSet<String>());
    }

    private int counter = 0;
    public void start(String gitHubLink) {
        counter ++;
        if (this.projects.add(gitHubLink)) {
            this.executor.execute(new GitHubLoaderTask(gitHubLink, this));
        }
    }

    public void notify(String github, String payload){
        try {
            HashMap<String,Object> result = new ObjectMapper().readValue(payload, HashMap.class);
            HashMap<String,Object> data = (HashMap<String,Object>)result.get("data");
            HashMap<String,Object> repository = (HashMap<String,Object>)data.get("repository");
            Document project = new Document(repository);
            project.put("_id", github);
            log.info("github: " + github);
            mongo.save("projects", project);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
    }
}