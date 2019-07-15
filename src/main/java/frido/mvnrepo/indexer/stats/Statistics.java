package frido.mvnrepo.indexer.stats;

public class Statistics {

	private WordCounter ciManagement;
	private WordCounter dependencies;
	private WordCounter licenses;
	private WordCounter organization;
	private WordCounter developers;
	private WordCounter contributors;
	private WordCounter distributionManagement;

	public Statistics() {
		ciManagement = new WordCounter();
		dependencies = new WordCounter();
		licenses = new WordCounter();
		organization = new WordCounter();
		developers = new WordCounter();
		contributors = new WordCounter();
		distributionManagement = new WordCounter();
	}

	public void addCiManagement(String key) {
		ciManagement.add(key);
	}

	public void addDependencies(String key, String subKey) {
		dependencies.add(key, subKey);
	}

	public void addLicenses(String key) {
		licenses.add(key);
	}

	public void addOrganization(String key) {
		organization.add(key);
	}

	public void addDevelopers(String key) {
		developers.add(key);
	}

	public void addContributors(String key) {
		contributors.add(key);
	}

	public void addDistributionManagement(String key) {
		distributionManagement.add(key);
	}
	
	public WordCounter getCiManagement() {
		return ciManagement;
	}

	public WordCounter getDependencies() {
		return dependencies;
	}

	public WordCounter getLicenses() {
		return licenses;
	}

	public WordCounter getOrganization() {
		return organization;
	}

	public WordCounter getDevelopers() {
		return developers;
	}

	public WordCounter getContributors() {
		return contributors;
	}

	public WordCounter getDistributionManagement() {
		return distributionManagement;
	}

}
