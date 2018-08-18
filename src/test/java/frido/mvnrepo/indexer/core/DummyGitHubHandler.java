package frido.mvnrepo.indexer.core;

import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadLink;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyGitHubHandler implements Consumer {

    Logger log = LoggerFactory.getLogger(DummyGitHubHandler.class);

    private Document response;

    public DummyGitHubHandler() {
    }

	@Override
	public void notify(DownloadLink url, String content) {
		try {
            HashMap<String, HashMap<String, HashMap<String, Object>>> result = new ObjectMapper().readValue(content, new TypeReference<HashMap<String, Object>>() {});
            HashMap<String, HashMap<String, Object>> data = result.get("data");
            HashMap<String, Object> repository = data.get("repository");
            response = new Document(repository);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void error(Throwable e){
        
    }
    
    public Document getResponse(){
        return this.response;
    }

}