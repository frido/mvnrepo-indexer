package frido.mvnrepo.indexer.pom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.download.Consumer;
import frido.mvnrepo.indexer.core.download.DownloadLink;

public class PomConsumer implements Consumer {

	Logger log = LoggerFactory.getLogger(PomConsumer.class);
	private Database db;

	public PomConsumer(Database database) {
		this.db = database;
	}

	@Override
	public void notify(DownloadLink url, String xml) {
		Pom pom = Pom.valueOf(xml);
		this.db.update("pom", pom.getUniqFilter(), pom.getDocument());
	}

	@Override
	public void error(Throwable e) {
	}
}