package frido.mvnrepo.indexer.stats;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
		Map<String, Integer> ciManagement = new HashMap<>();
		int errors = 0;

		try (BufferedReader br = new BufferedReader(new FileReader("pom.db"))) {
			String line;
			while ((line = br.readLine()) != null) {
				try {
					BasicDBObject doc = (BasicDBObject) JSON.parse(line);
					BasicDBObject ciDoc = (BasicDBObject) doc.get("IssueManagement");
					if (ciDoc != null) {
						extractBase(ciDoc.getString("Url")).ifPresent(url -> addToMap(ciManagement, url));
					}
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

		Map<String, Integer> sortedCiManagement = sortByValue(ciManagement);
		printMap(sortedCiManagement, 10);
		System.out.println("keySize: " + ciManagement.keySet().size());
		IntSummaryStatistics xxx = ciManagement.values().stream().collect(Collectors.summarizingInt(i -> i));
		System.out.println("valueSize: " + xxx);
	}

	private void addToMap(Map<String, Integer> map, String key) {
		Integer count = map.get(key);
		if (count == null) {
			count = 1;
		}
		map.put(key, ++count);
	}

	private Optional<String> extractBase(String link) {
		if (link == null) {
			return Optional.empty();
		}
		Matcher m = p.matcher(link);
		while (m.find()) {
			String base = m.group(1);
			// System.out.println(base + " <- " + link);
			return Optional.ofNullable(base);
		}
		return Optional.empty();
	}

	private Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	public <K, V> void printMap(Map<K, V> map, int limit) {
		for (Map.Entry<K, V> entry : map.entrySet()) {
			limit--;
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
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