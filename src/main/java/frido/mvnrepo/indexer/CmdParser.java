package frido.mvnrepo.indexer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

// TODO: help description
public class CmdParser {

	private Options options = new Options();
	private CommandLine cmd;

	public CmdParser() {
		options.addOption("metadata", false, "download metadata files");
		options.addOption("pom", false, "download pom files");
		options.addOption("github", false, "download github projects");
	}

	public void parse(String[] args) throws ParseException {
		CommandLineParser parser = new DefaultParser();
		cmd = parser.parse(options, args);
	}

	public boolean metadataFlag() {
		return cmd.hasOption("metadata");
	}

	public boolean pomFlag() {
		return cmd.hasOption("pom");
	}

	public boolean githubFlag() {
		return cmd.hasOption("github");
	}
}
