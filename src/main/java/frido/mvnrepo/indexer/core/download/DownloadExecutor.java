package frido.mvnrepo.indexer.core.download;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadExecutor implements Executor {

	Logger log = LoggerFactory.getLogger(DownloadExecutor.class);

	private ExecutorService executor;
	private AtomicInteger counter = new AtomicInteger(0);

	public DownloadExecutor(ExecutorService e) {
		this.executor = e;
	}

	@Override
	public void execute(Runnable command) {
		this.executor.execute(command);
	}

	public boolean isTerminated() {
		return this.executor.isTerminated();
	}

	public void increment() {
		counter.incrementAndGet();
	}

	public void decrementOrFinish() {
		if (counter.decrementAndGet() == 0) {
			this.executor.shutdown();
		}
	}

	public void waitForTerminate() {
		while (!executor.isTerminated()) {
			// wait while the process is active
		}
	}

	public int getStatus() {
		return counter.get();
	}

}