package frido.mvnrepo.indexer.tdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.URI;

import org.junit.Test;

import frido.mvnrepo.indexer.core.client.ClientException;
import frido.mvnrepo.indexer.core.client.HttpClient;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadClient;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLink;
import frido.mvnrepo.indexer.core.download.DownloadLinkClient;
import frido.mvnrepo.indexer.core.download.DownloadManager;
import frido.mvnrepo.indexer.core.download.Link;

public class DownloaderTest {

	private String HTTP_CONTENT = "http content";

	@Test
	public void start() {
		Link downloadLink = new DownloadLink("http://test.com");
		DownloadExecutor executor = new DownloadExecutor(new NoThreadExecutor());
		DownloadClient httpClient = new DownloadLinkClient(getMockClient());
		Consumer consumer = new Consumer() {

			@Override
			public void error(Exception e) {
				fail("Unexpected Error");
			}

			@Override
			public void accept(Link link, String content) {
				assertEquals(downloadLink, link);
				assertEquals(HTTP_CONTENT, content);
			}
		};
		DownloadManager processor = new DownloadManager(httpClient, executor);
		processor.download(downloadLink, consumer);
	}

	// mock clien je tiez v inom teste
	private HttpClient getMockClient() {
		return new HttpClient() {

			@Override
			public String get(URI url) throws ClientException {
				return HTTP_CONTENT;
			}

			@Override
			public String post(URI url, String body) throws ClientException {
				throw new UnsupportedOperationException();
			}
		};
	}

}
