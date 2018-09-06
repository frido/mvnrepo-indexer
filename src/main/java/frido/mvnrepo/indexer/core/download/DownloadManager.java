package frido.mvnrepo.indexer.core.download;

import java.net.URISyntaxException;

import frido.mvnrepo.indexer.core.client.ClientException;

public class DownloadManager {
	private DownloadClient client;
	private DownloadExecutor executor;

	public DownloadManager(DownloadClient client, DownloadExecutor executorService) {
		this.client = client;
		this.executor = executorService;
	}

	public void download(Link link, Consumer consumer) {
		this.executor.increment();
		this.executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String content = null;
					try {
						content = client.download(link);
					} catch (ClientException | URISyntaxException e) {
						consumer.error(e);
						return;
					}
					consumer.accept(link, content);
				} finally {
					executor.decrementOrFinish();
				}
			}

			@Override
			public String toString() {
				return link.toString();
			}
		});
	}

	public String downloadSync(Link link) throws ClientException, URISyntaxException {
		return client.download(link);
	}

}
