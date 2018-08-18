package frido.mvnrepo.indexer.github;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.Consumer;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubHandler implements Consumer {

    Logger log = LoggerFactory.getLogger(GitHubHandler.class);

    private Database db;
    private Executor executor;
    private int size;
    private AtomicInteger counter = new AtomicInteger(0);

    public GitHubHandler(Database database, Executor executor, int size) {
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
        }
    }
    
    @Override
    public void error(Throwable e){

    }
}