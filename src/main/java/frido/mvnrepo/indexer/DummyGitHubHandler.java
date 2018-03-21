package frido.mvnrepo.indexer;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bson.Document;

public class DummyGitHubHandler implements Consumer {

    private Document response;

    public DummyGitHubHandler() {
    }

	@Override
	public void notify(String content) {
		try {
            HashMap<String, Object> result = new ObjectMapper().readValue(content, HashMap.class);
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            HashMap<String, Object> repository = (HashMap<String, Object>) data.get("repository");
            this.response = new Document(repository);
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
    
    public Document getResponse(){
        return this.response;
    }

}