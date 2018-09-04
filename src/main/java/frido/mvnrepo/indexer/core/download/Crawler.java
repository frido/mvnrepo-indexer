package frido.mvnrepo.indexer.core.download;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Crawler implements Consumer {

	Logger log = LoggerFactory.getLogger(Crawler.class);

	private static String LINK_PATTERN = "<a href=\"(.*?)\"";
	private static Pattern p = Pattern.compile(LINK_PATTERN);

	private DownloadExecutor executor;
	private DownloadClient client;
	private Consumer consumer;
	private String filter;

	public Crawler(DownloadClient client, DownloadExecutor executor, Consumer consumer) {
		this.client = client;
		this.executor = executor;
		this.consumer = consumer;
	}

	public void search(Link link, String filter) {
		this.filter = filter;
		download(link);
	}

	private void search(Link link) {
		download(link);
	}

	public void download(Link link) {
		DownloadManager processor = new DownloadManager(client, executor);
		processor.download(link, this);
	}

	public void match(Link link) {
		DownloadManager processor = new DownloadManager(client, executor);
		processor.download(link, consumer);
	}

	@Override
	public void accept(Link link, String content) {
		List<Link> links = getLinks(link, content);
		for (Link item : links) {
			if (item.match(this.filter)) {
				this.match(item);
			} else if (item.isDirectory()) {
				this.search(item);
			}
		}
	}

	private List<Link> getLinks(Link link, String content) {
		List<Link> links = new LinkedList<>();
		Matcher m = p.matcher(content);
		while (m.find()) {
			links.add(link.append(m.group(1)));
		}
		return links;
	}

	@Override
	public void error(Exception e) {
		// TODO Auto-generated method stub

	}
}
