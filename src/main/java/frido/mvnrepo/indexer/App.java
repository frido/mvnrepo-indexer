package frido.mvnrepo.indexer;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.db.MongoDatabase;
import frido.mvnrepo.indexer.core.download.ComparableExecutor;
import frido.mvnrepo.indexer.core.download.DownloadExecutor;
import frido.mvnrepo.indexer.github.GitHubProcessor;
import frido.mvnrepo.indexer.pom.PomProcessor;

public class App {

    Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        Database database = new MongoDatabase();
        DownloadExecutor executor =  new DownloadExecutor(new ComparableExecutor(5));

        List<String> arguments = Arrays.asList(args);
        if(arguments.isEmpty() || arguments.contains("metadata")) {
            // MetadataHandler handler = new MetadataHandler(database, executor);
            // MetadataProcessor process1 = new MetadataProcessor(handler);
            // process1.start("http://central.maven.org/maven2/");
        }
        if(arguments.contains("pom")) {
            PomProcessor process2 = new PomProcessor(database, executor);
            process2.start();
        }
        if(arguments.contains("github")) {
            GitHubProcessor process3 = new GitHubProcessor(database, executor);
            process3.start();
        }
    }
}
