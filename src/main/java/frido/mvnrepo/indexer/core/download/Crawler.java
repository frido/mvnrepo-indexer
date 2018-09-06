package frido.mvnrepo.indexer.core.download;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Crawler implements Consumer {

	private Logger log = LoggerFactory.getLogger(Crawler.class);

	private static String LINK_PATTERN = "<a href=\"(.*?)\"";
	private static Pattern p = Pattern.compile(LINK_PATTERN);

	protected DownloadManager manager;
	protected Consumer consumer;
	protected String filter;

	public Crawler(DownloadClient client, DownloadExecutor executor, Consumer consumer) {
		manager = new DownloadManager(client, executor);
		this.consumer = consumer;
	}

	public void search(Link link, String filter) {
		this.filter = filter;
		download(link);
	}

	public void download(Link link) {
		manager.download(link, this);
	}

	public void match(Link link) {
		manager.download(link, consumer);
	}

	@Override
	public void accept(Link link, String content) {
		List<Link> links = getLinks(link, content);
		for (Link item : links) {
			if (item.match(this.filter)) {
				this.match(item);
			} else if (item.isDirectory()) {
				this.download(item);
			}
		}
	}

	protected List<Link> getLinks(Link link, String content) {
		List<Link> links = new LinkedList<>();
		Matcher m = p.matcher(content);
		while (m.find()) {
			links.add(link.append(m.group(1)));
		}
		return links;
	}

	@Override
	public void error(Exception e) {
		log.error("", e);
	}
}
