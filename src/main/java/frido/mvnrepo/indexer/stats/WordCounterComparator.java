package frido.mvnrepo.indexer.stats;

import java.util.Comparator;

public class WordCounterComparator implements Comparator<Entry> {

	@Override
	public int compare(Entry o1, Entry o2) {
		return o2.getValue().compareTo(o1.getValue());
	}

}
