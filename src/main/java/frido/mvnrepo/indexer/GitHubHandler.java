package frido.mvnrepo.indexer;

import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubHandler implements Consumer {

    Logger log = LoggerFactory.getLogger(GitHubHandler.class);

    private Database db;

    public GitHubHandler(Database database) {
        this.db = database;
    }

	@Override
	public void notify(String content) {
		try {
            HashMap<String, HashMap<String, HashMap<String, Object>>> result = new ObjectMapper().readValue(content, new TypeReference<HashMap<String, Object>>() {});
            HashMap<String, HashMap<String, Object>> data = (HashMap<String, HashMap<String, Object>>) result.get("data");
            HashMap<String, Object> repository = (HashMap<String, Object>) data.get("repository");
            Document project = new Document(repository);
            this.db.save("projects", project);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
	}

}