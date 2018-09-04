package frido.mvnrepo.indexer.github;

import java.net.URI;

public class GitHubUrl {
	private static final String HTTPS_GITHUB_COM = "https://github.com/";

	// private String url;
	private String owner;
	private String repo;

	GitHubUrl(URI link) {
		// this.url = link;
		String link1 = link.toString() + "/";
		String part1 = link1.substring(HTTPS_GITHUB_COM.length());
		owner = part1.substring(0, part1.indexOf('/'));
		String part2 = part1.substring(owner.length() + 1);
		repo = part2.substring(0, part2.indexOf('/'));
	}

	// TODO: convert to static: GitHubUrl.query(URI link)
	public String query() {
		return String.format(
				"{ \"query\": \"query{repository(owner:\\\"%s\\\", name:\\\"%s\\\"){owner {login} name createdAt description homepageUrl pushedAt stargazers {totalCount}watchers{totalCount}forks{totalCount}}}\"}",
				owner, repo);
	}
}