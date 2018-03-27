package frido.mvnrepo.indexer;

import java.util.concurrent.Executor;

class NoThreadExecutor implements Executor {

	@Override
	public void execute(Runnable command) {
		command.run();
	}

}