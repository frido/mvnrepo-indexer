package frido.mvnrepo.indexer;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bson.Document;

public class GitHubHandler implements Consumer {

    private Database db;

    public GitHubHandler(Database database) {
        this.db = database;
    }

	@Override
	public void notify(String content) {
		try {
            HashMap<String, Object> result = new ObjectMapper().readValue(content, HashMap.class);
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            HashMap<String, Object> repository = (HashMap<String, Object>) data.get("repository");
            Document project = new Document(repository);
            this.db.save("projects", project);
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