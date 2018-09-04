package frido.mvnrepo.indexer.tdd.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.download.ComparableExecutor;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.Crawler;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.DownloadLink;
import frido.mvnrepo.indexer.core.download.DownloadLinkClient;
import frido.mvnrepo.indexer.core.download.Link;

public class CrawlerIT {

	Logger log = LoggerFactory.getLogger(CrawlerIT.class);

	@Test
	public void start() {
		List<Link> links = Collections.synchronizedList(new LinkedList<>());
		DownloadExecutor executor = new DownloadExecutor(new ComparableExecutor(5));

		Crawler clawler = new Crawler(new DownloadLinkClient(new JerseyHttpClient()), executor, new Consumer() {

			@Override
			public void error(Exception e) {
				fail("Excaption");
			}

			@Override
			public void accept(Link link, String content) {
				links.add(link);

			}
		});
		clawler.search(new DownloadLink("http://central.maven.org/maven2/ant/"), "maven-metadata.xml");

		executor.waitForTerminate();

		assertEquals(217, links.size());
		for (Link link : links) {
			log.info(link.toString());
			assertTrue(link.toString().endsWith("maven-metadata.xml"));
		}
	}

}
