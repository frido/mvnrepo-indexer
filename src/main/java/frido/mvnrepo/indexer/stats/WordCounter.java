package frido.mvnrepo.indexer.stats;

import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WordCounter {

	private Map<String, Entry> map = new HashMap<>();

	public void add(String key) {
		if (key == null) {
			return;
		}
		Entry value = map.get(key);
		if (value == null) {
			value = new Entry(key);
		}
		value.add();
		map.put(key, value);
	}

	public List<Entry> getWords() {
		return map.values().stream().sorted(new WordCounterComparator()).collect(Collectors.toList());
	}
	
	public IntSummaryStatistics getStatistics() {
		return map.values().stream().collect(Collectors.summarizingInt(e -> {
			return e.getValue().intValue();
		}));
	}
}
