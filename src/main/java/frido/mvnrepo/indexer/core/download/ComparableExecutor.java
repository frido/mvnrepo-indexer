package frido.mvnrepo.indexer.core.download;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ComparableExecutor extends ThreadPoolExecutor {
	public ComparableExecutor(int poolSize) {
		super(poolSize, poolSize, 0, TimeUnit.HOURS, new PriorityBlockingQueue<Runnable>(100, new DeepComparator()));
	}
}