package frido.mvnrepo.indexer.stats;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBList;
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
					
					getList(doc, "Dependencies").ifPresent(list -> {
						list.forEach(d -> {
							String key = getString((BasicDBObject) d, "GroupId").orElse("null") + ":" + getString((BasicDBObject) d, "ArtifactId").orElse("null");
							String version = getString((BasicDBObject) d, "Version").orElse("null");
							statistics.addDependencies(key, version);
						});
					});
//					.map(e -> {
//						
////						return 
//					})
//					.ifPresent(key -> statistics.addDependencies(key));
					
					getList(doc, "Licenses").ifPresent(list -> {
						list.forEach(d -> {
							String key = getString((BasicDBObject) d, "Name").orElse("null");
							statistics.addLicenses(key);
						});
					});
					
					getObject(doc, "Organization")
					.flatMap(e -> getString(e, "Name"))
					.ifPresent(key -> statistics.addOrganization(key));

					getList(doc, "Developers").ifPresent(list -> {
						list.forEach(d -> {
							String key = getString((BasicDBObject) d, "Name").orElse("null");
							statistics.addDevelopers(key);
						});
					});
					getList(doc, "Contributors").ifPresent(list -> {
						list.forEach(d -> {
							String key = getString((BasicDBObject) d, "Name").orElse("null");
							statistics.addContributors(key);
						});
					});
					
					//issueManagement
					
					getObject(doc, "CiManagement")
					.flatMap(e -> getString(e, "Url"))
					.flatMap(url -> extractBase(url))
					.ifPresent(key -> statistics.addCiManagement(key));
					
					getObject(doc, "DistributionManagement")
					.flatMap(e -> getObject(e, "Repository"))
					.flatMap(e -> getString(e, "Url"))
					.ifPresent(key -> statistics.addDistributionManagement(key));

				} catch (Exception e) {
					e.printStackTrace();
					errors++;
					//return;
				}
			}
		} catch (FileNotFoundException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}

		System.out.println("ERRORS: " + errors);

		print(statistics.getCiManagement());
		print(statistics.getContributors());
		print(statistics.getDependencies());
		print(statistics.getDevelopers());
		print(statistics.getDistributionManagement());
		print(statistics.getLicenses());
		print(statistics.getOrganization());
	}
	
	private void print(WordCounter counter) {
		printMap(counter, 20);
		System.out.println(counter.getStatistics());
	}

	private Optional<BasicDBObject> getObject(BasicDBObject doc, String prop) {
		return Optional.ofNullable((BasicDBObject) doc.get(prop));
	}
	
	private Optional<BasicDBList> getList(BasicDBObject doc, String prop) {
		return Optional.ofNullable((BasicDBList) doc.get(prop));
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

	public <K, V> void printMap(WordCounter counter, int limit) {
		int limit2 = limit;
		for (Entry e : counter.getWords()) {
			limit--;
			System.out.println("Key : " + e.getKey() + ", Value : " + e.getValue());
			for (Entry subE : counter.getSubWords(e.getKey())) {
				System.out.println("--- Key : " + subE.getKey() + ", Value : " + subE.getValue());
				limit2--;
				if (limit2 == 0) {
					break;
				}
			}
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