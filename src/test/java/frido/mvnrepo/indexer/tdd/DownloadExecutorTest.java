package frido.mvnrepo.indexer.tdd;

import static org.junit.Assert.*;

import java.util.concurrent.Executors;

import org.junit.Test;

import frido.mvnrepo.indexer.core.download.DownloadExecutor;

public class DownloadExecutorTest {

	@Test
	public void incrementDecrementFinish() {
		DownloadExecutor de = new DownloadExecutor(Executors.newFixedThreadPool(1));
		de.increment();
		de.increment();
		assertFalse(de.isTerminated());
		de.decrementOrFinish();
		assertFalse(de.isTerminated());
		de.decrementOrFinish();
		assertTrue(de.isTerminated());
	}

}
