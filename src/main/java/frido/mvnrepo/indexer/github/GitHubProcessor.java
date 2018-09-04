package frido.mvnrepo.indexer.github;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import frido.mvnrepo.indexer.core.client.ClientException;
import frido.mvnrepo.indexer.core.client.HttpClient;
import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadClient;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLink;
import frido.mvnrepo.indexer.core.download.DownloadManager;
import frido.mvnrepo.indexer.core.download.Link;
import frido.mvnrepo.indexer.data.Project;

public class GitHubProcessor implements DownloadClient, Consumer {

	Logger log = LoggerFactory.getLogger(GitHubProcessor.class);
	private URI github = null;

	private HttpClient client;
	private Database database;
	private DownloadExecutor executor;

	public GitHubProcessor(Database database, HttpClient client, DownloadExecutor executorService) {
		this.database = database;
		this.client = client;
		this.executor = executorService;
		try {
			this.github = new URI("https://api.github.com/graphql");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		DownloadManager processor = new DownloadManager(this, executor);
		for (Document doc : database.findByUrlWithGithub()) {
			DownloadLink link = new DownloadLink(doc.getString("Url"));
			processor.download(link, this);
		}
	}

	@Override
	public void accept(Link link, String content) {
		try {
			HashMap<String, HashMap<String, HashMap<String, Object>>> result = new ObjectMapper().readValue(content,
					new TypeReference<HashMap<String, Object>>() {
					});
			HashMap<String, HashMap<String, Object>> data = result.get("data");
			HashMap<String, Object> repository = data.get("repository");
			Project project = new Project(new Document(repository));
			this.database.update("projects", project.getUniqFilter(), project.getDocument());
		} catch (Exception e) {
			// log.error(content, e);
		}

	}

	@Override
	public void error(Exception e) {
		log.error("", e);

	}

	@Override
	public String download(Link link) throws ClientException, URISyntaxException {
		return this.client.post(github, new GitHubUrl(link.getURI()).query()); // TODO: lepsi pattern na konvertovanie
	}
}