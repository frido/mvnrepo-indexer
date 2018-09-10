package frido.mvnrepo.indexer;

import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

// TODO: help description
public class CmdParser {

	private Options options = new Options();
	private CommandLine cmd;

	public CmdParser() {
//		options.addOption("m", true, "download metadata files");
		options.addOption("p", false, "download pom files");
		options.addOption("g", false, "download github projects");
		options.addOption("t", true, "number of threads");
		options.addOption("s", false, "print statistics");
		Option m = Option.builder("m").argName("repository").hasArg(true).optionalArg(true)
				.desc("search given repository").build();
		options.addOption(m);
	}

	public void parse(String[] args) throws ParseException {
		CommandLineParser parser = new DefaultParser();
		cmd = parser.parse(options, args);
	}

	public boolean metadataFlag() {
		return cmd.hasOption("m");
	}

	public String repository() {
		return Optional.ofNullable(cmd.getOptionValue("m")).orElse("http://central.maven.org/maven2/");
	}

	public String threadsCount() {
		return Optional.ofNullable(cmd.getOptionValue("t")).orElse("50");
	}

	public boolean pomFlag() {
		return cmd.hasOption("p");
	}

	public boolean statisticsFlag() {
		return cmd.hasOption("s");
	}

	public boolean githubFlag() {
		return cmd.hasOption("g");
	}

	public void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("mvnRepo-indexer", options);
	}
}
