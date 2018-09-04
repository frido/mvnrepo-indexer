package frido.mvnrepo.indexer.core.download;

import java.net.URI;
import java.net.URISyntaxException;

public interface Link {

	URI getURI() throws URISyntaxException;

	boolean match(String filter);

	boolean isDirectory();

	Link append(String group);

}
