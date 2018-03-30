package frido.mvnrepo.indexer;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        Database database = new MongoDatabase();

        List<String> arguments = Arrays.asList(args);
        if(arguments.isEmpty() || arguments.contains("metadata")) {
            MetadataProcessor process1 = new MetadataProcessor(database);
            process1.start();
        }
        if(arguments.contains("pom")) {
            PomProcessor process2 = new PomProcessor(database);
            process2.start();
        }
        if(arguments.contains("github")) {
            GitHubProcessor process3 = new GitHubProcessor(database);
            process3.start();
        }
    }
}
