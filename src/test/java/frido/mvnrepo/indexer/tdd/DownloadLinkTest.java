package frido.mvnrepo.indexer.tdd;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;

import org.junit.Test;

import frido.mvnrepo.indexer.core.download.DownloadLink;
import frido.mvnrepo.indexer.core.download.Link;

public class DownloadLinkTest {

	@Test
	public void getURI() throws URISyntaxException {
		String http = "http://test.com";
		Link link = new DownloadLink(http);
		assertEquals(http, link.getURI().toString());
	}

}
