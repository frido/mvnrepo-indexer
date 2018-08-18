package frido.mvnrepo.indexer.core.download;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadExecutor implements Executor{

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

    public void increment(){
        counter.incrementAndGet();
    }

    public void decrementOrFinish(){
        if(counter.decrementAndGet() == 0){
            this.executor.shutdown();
        }
    }

}