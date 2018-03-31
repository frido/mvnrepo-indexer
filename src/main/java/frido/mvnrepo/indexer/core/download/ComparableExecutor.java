package frido.mvnrepo.indexer.core.download;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ComparableExecutor extends ThreadPoolExecutor {
    public ComparableExecutor(int poolSize) {
        super(poolSize, poolSize, 0, TimeUnit.HOURS,
                new PriorityBlockingQueue<Runnable>(100, (Runnable r1, Runnable r2) -> {
                    Prioritable t1 = (Prioritable) r1;
                    Prioritable t2 = (Prioritable) r2;
                    return (-1) * (t1.getPriority() - t2.getPriority());
                }));
    }

}