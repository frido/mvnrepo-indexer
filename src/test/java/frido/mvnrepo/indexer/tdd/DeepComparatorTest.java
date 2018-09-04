package frido.mvnrepo.indexer.tdd;

import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.download.DeepComparator;

public class DeepComparatorTest {

	Logger log = LoggerFactory.getLogger(CrawlerExtractorRun.class);

	@Test
	public void start() {
        String longLink = "http://central.maven.org/maven2/io/vertx/vertx-config/3.5.3.CR1/";
        String shortLink = "http://central.maven.org/maven2/io/";
        DeepComparator comparator = new DeepComparator();
        int diff = comparator.compare(new MockRunable(longLink), new MockRunable(shortLink));
        assertTrue(diff < 0);
    }

    class MockRunable implements Runnable {

        private String text;

        MockRunable(String str) {
            this.text = str;
        }

        @Override
        public void run() {

        }

        @Override 
        public String toString(){
            return text.toString();
        }

    }
}