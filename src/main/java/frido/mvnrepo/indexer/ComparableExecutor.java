package frido.mvnrepo.indexer;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ComparableExecutor extends ThreadPoolExecutor {
    ComparableExecutor(int poolSize){
        super(poolSize, poolSize, 0, TimeUnit.HOURS, new PriorityBlockingQueue<Runnable>(100, new Comparator<Runnable>() {
            @Override
            public int compare(Runnable o1, Runnable o2) {
                Prioritable t1 = (Prioritable) o1;
                Prioritable t2 = (Prioritable) o2;
                return (-1) * (t1.getPriority() - t2.getPriority()); // reverse order
            }
            }));
    }
}