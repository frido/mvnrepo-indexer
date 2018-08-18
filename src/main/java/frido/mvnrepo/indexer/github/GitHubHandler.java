package frido.mvnrepo.indexer.github;

import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadLink;

public class GitHubHandler implements Consumer {

    Logger log = LoggerFactory.getLogger(GitHubHandler.class);

    private Database db;

    public GitHubHandler(Database database) {
        this.db = database;
    }

	@Override
	public void notify(DownloadLink link, String content) {
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