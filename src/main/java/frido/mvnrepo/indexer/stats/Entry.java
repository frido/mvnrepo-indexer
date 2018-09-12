package frido.mvnrepo.indexer.stats;

public class Entry {
	private String key;
	private Long value;

	public Entry(String key) {
		this.key = key;
		value = 0l;
	}

	public void add() {
		value = value + 1;
	}

	public String getKey() {
		return key;
	}

	public Long getValue() {
		return value;
	}

}
