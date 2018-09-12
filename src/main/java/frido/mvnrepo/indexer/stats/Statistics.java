package frido.mvnrepo.indexer.stats;

public class Statistics {

	private WordCounter ciManagement;

	public Statistics() {
		ciManagement = new WordCounter();
	}

	public void addCiManagement(String key) {
		ciManagement.add(key);
	}

	public WordCounter getCiManagement() {
		return ciManagement;
	}

}
