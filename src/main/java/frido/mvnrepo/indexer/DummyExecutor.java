package frido.mvnrepo.indexer;

import java.util.concurrent.Executor;

class DummyExecutor implements Executor {

	@Override
	public void execute(Runnable command) {
		command.run();
	}

}