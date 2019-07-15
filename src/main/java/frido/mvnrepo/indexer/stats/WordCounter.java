package frido.mvnrepo.indexer.stats;

import java.util.Collections;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WordCounter {

	private Map<String, Entry> map = new HashMap<>();
	private Map<String, WordCounter> map2 = new HashMap<>();

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
	
	public void add(String key, String subKey) {
		add(key);
		WordCounter subCounter = map2.get(key);
		if (subCounter == null) {
			subCounter = new WordCounter();
		}
		subCounter.add(subKey);
		map2.put(key, subCounter);
		
	}

	public List<Entry> getWords() {
		return map.values().stream().sorted(new WordCounterComparator()).collect(Collectors.toList());
	}
	
	public List<Entry> getSubWords(String key) {
		if (map2.get(key) == null) {
			return Collections.emptyList();
		}
		return map2.get(key).getWords();
	}
	
	public IntSummaryStatistics getStatistics() {
		return map.values().stream().collect(Collectors.summarizingInt(e -> {
			return e.getValue().intValue();
		}));
	}
}
