package frido.mvnrepo.indexer.tdd.integration;

import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import frido.mvnrepo.indexer.core.client.ClientException;
import frido.mvnrepo.indexer.core.client.HttpClient;
import frido.mvnrepo.indexer.core.client.JerseyHttpClient;

public class JerseyHttpClientIT {

	@Test
	public void get() throws ClientException, URISyntaxException {
		String http = "https://jsonplaceholder.typicode.com/todos/1";
		HttpClient client = new JerseyHttpClient();
		String content = client.get(new URI(http));
		assertTrue(content.contains("\"id\": 1,"));
	}

	@Test
	public void post() throws ClientException, URISyntaxException {
		String http = "https://jsonplaceholder.typicode.com/posts";
		HttpClient client = new JerseyHttpClient();
		String content = client.post(new URI(http), "{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }");
		assertTrue(content.contains("\"id\": 101,"));
	}

}
