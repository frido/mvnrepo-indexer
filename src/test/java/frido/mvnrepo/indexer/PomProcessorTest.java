package frido.mvnrepo.indexer;

import static org.junit.Assert.assertEquals;

import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.MockDatabase;
import frido.mvnrepo.indexer.core.NoThreadExecutor;
import frido.mvnrepo.indexer.core.client.Client;
import frido.mvnrepo.indexer.core.client.DownloadClient;
import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.core.download.Downloader;
import frido.mvnrepo.indexer.core.download.MetadataProvider;
import frido.mvnrepo.indexer.core.download.Provider;
import frido.mvnrepo.indexer.pom.PomConsumer;
import frido.mvnrepo.indexer.pom.PomProcessor;

public class PomProcessorTest {

	Logger log = LoggerFactory.getLogger(PomProcessor.class);

	@Test
	public void testDownloader() throws Exception {

		MockDatabase database = new MockDatabase();
		DownloadExecutor executor = new DownloadExecutor(new NoThreadExecutor());

		Provider provider = new MetadataProvider(database);
		Consumer consumer = new PomConsumer(database);
		Client httpClient = new DownloadClient(new JerseyHttpClient());
		Downloader loader = new Downloader(executor, httpClient);
		PomProcessor process2 = new PomProcessor(provider, loader, consumer);
		process2.start();

		assertEquals(1, database.getUpdated().size());
		Document pom = database.getUpdated().get(0);
		assertEquals("org.apache.abdera", pom.getString("GroupId"));
		assertEquals("abdera", pom.getString("ArtifactId"));
		assertEquals("1.1.3", pom.getString("Version"));
	}
}
