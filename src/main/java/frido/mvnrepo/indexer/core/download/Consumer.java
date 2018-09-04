package frido.mvnrepo.indexer.core.download;

public interface Consumer {

	void accept(Link link, String content);

	void error(Exception e);
}
