package frido.mvnrepo.indexer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.artifact.ArtifactHandler;
import frido.mvnrepo.indexer.artifact.ArtifactProcessor;
import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.db.MongoDatabase;
import frido.mvnrepo.indexer.core.download.ComparableExecutor;
import frido.mvnrepo.indexer.github.GitHubProcessor;
import frido.mvnrepo.indexer.pom.PomProcessor;

public class App {

    Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        Database database = new MongoDatabase();
        ExecutorService executor =  new ComparableExecutor(5);

        List<String> arguments = Arrays.asList(args);
        if(arguments.isEmpty() || arguments.contains("metadata")) {
            ArtifactHandler handler = new ArtifactHandler(database, executor);
            ArtifactProcessor process1 = new ArtifactProcessor(handler);
            process1.start("http://central.maven.org/maven2/");
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
