package frido.mvnrepo.indexer.stats;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import frido.mvnrepo.indexer.core.db.Datasource;
import frido.mvnrepo.indexer.core.db.Repository;
import frido.mvnrepo.indexer.data.Pom;
import frido.mvnrepo.indexer.metadata.MetadataProcessor;
import frido.mvnrepo.indexer.pom.PomConverter;
import frido.mvnrepo.indexer.pom.PomRepository;

public class StatProcessor {

	private static String LINK_PATTERN = "https?:\\/\\/(.*?)\\/";
	private static Pattern p = Pattern.compile(LINK_PATTERN);

	Logger log = LoggerFactory.getLogger(MetadataProcessor.class);

	private Repository<Pom> repo;

	public StatProcessor(Datasource ds) {
		this.repo = new PomRepository(ds, new PomConverter());
	}

	public void start() {
		Statistics statistics = new Statistics();
		int errors = 0;
		try (BufferedReader br = new BufferedReader(new FileReader("pom.db"))) {
			String line;
			while ((line = br.readLine()) != null) {
				try {
					BasicDBObject doc = (BasicDBObject) JSON.parse(line);

					getObject(doc, "IssueManagement")
						.flatMap(im -> getString(im, "Url"))
						.flatMap(url -> extractBase(url))
						.ifPresent(key -> statistics.addCiManagement(key));

				} catch (Exception e) {
					errors++;
					// log.error("JSON.parse(line) ERROR");
				}
			}
		} catch (FileNotFoundException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}

		System.out.println("ERRORS: " + errors);

		printMap(statistics.getCiManagement().getWords(), 10);
		System.out.println(statistics.getCiManagement().getStatistics());
	}

	private Optional<BasicDBObject> getObject(BasicDBObject doc, String prop) {
		return Optional.ofNullable((BasicDBObject) doc.get(prop));
	}

	private Optional<String> getString(BasicDBObject doc, String prop) {
		return Optional.ofNullable(doc.getString(prop));
	}

	private Optional<String> extractBase(String link) {
		if (link == null) {
			return Optional.empty();
		}
		Matcher m = p.matcher(link);
		while (m.find()) {
			String base = m.group(1);
			return Optional.ofNullable(base);
		}
		return Optional.empty();
	}

	public <K, V> void printMap(List<Entry> list, int limit) {
		for (Entry e : list) {
			limit--;
			System.out.println("Key : " + e.getKey() + " Value : " + e.getValue());
			if (limit == 0) {
				return;
			}
		}
	}

	private boolean isGitHub(String link) {
		if (link != null && link.startsWith("https://github.com/")) {
			return true;
		}
		return false;
	}
}