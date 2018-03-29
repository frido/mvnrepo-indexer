package frido.mvnrepo.indexer;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubHandler implements Consumer {

    Logger log = LoggerFactory.getLogger(GitHubHandler.class);

    private Database db;
    private ExecutorService executor;
    private int size;
    private AtomicInteger counter = new AtomicInteger(0);

    public GitHubHandler(Database database, ExecutorService executor, int size) {
        this.db = database;
        this.executor = executor;
        this.size = size;
    }

	@Override
	public void notify(String url, String content) {
		try {
            HashMap<String, HashMap<String, HashMap<String, Object>>> result = new ObjectMapper().readValue(content, new TypeReference<HashMap<String, Object>>() {});
            HashMap<String, HashMap<String, Object>> data = result.get("data");
            HashMap<String, Object> repository = data.get("repository");
            Project project = new Project(new Document(repository));
            this.db.update("projects", project.getUniqFilter(), project.getDocument());
        } catch (Exception e) {
            log.error(content, e);
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