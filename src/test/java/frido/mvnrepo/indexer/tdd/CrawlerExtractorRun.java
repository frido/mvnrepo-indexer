package frido.mvnrepo.indexer.tdd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrawlerExtractorRun {

	Logger log = LoggerFactory.getLogger(CrawlerExtractorRun.class);

//	@Test
//	public void start() {
//		DownloadExecutor executor = new DownloadExecutor(new ComparableExecutor(20));
//		Database database = new MongoDatabase();
//		Consumer consumer = new MetadataProcessor(database);
//
//		Crawler clawler = new Crawler(new JerseyHttpClient(), executor, consumer);
//		clawler.search(new DownloadLink("http://central.maven.org/maven2/"), "maven-metadata.xml");
//
//		ScheduledThreadPoolExecutor s = new ScheduledThreadPoolExecutor(1);
//		s.scheduleAtFixedRate(() -> {
//			System.out.println(executor.getStatus());
//		}, 0, 10, TimeUnit.SECONDS);
//		executor.waitForTerminate();
//
//	}

}
