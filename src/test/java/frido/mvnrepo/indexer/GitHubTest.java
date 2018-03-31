package frido.mvnrepo.indexer;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.DummyGitHubHandler;
import frido.mvnrepo.indexer.core.NoThreadExecutor;
import frido.mvnrepo.indexer.core.client.Client;
import frido.mvnrepo.indexer.core.client.JerseyHttpClient;
import frido.mvnrepo.indexer.core.db.MongoDatabase;
import frido.mvnrepo.indexer.core.download.Downloader;
import frido.mvnrepo.indexer.github.GitHubClient;

public class GitHubTest {

    Logger log = LoggerFactory.getLogger(GitHubTest.class);

    @Test
    public void testDownloader() {
        DummyGitHubHandler consumer = new DummyGitHubHandler();
        Executor executor = new NoThreadExecutor();
        Client httpClient = new GitHubClient(
            new JerseyHttpClient("frido", System.getenv().get("GITHUB_KEY")));
        //GitHubLoader loader = new GitHubLoader(executor, consumer, httpClient);
        Downloader loader = new Downloader(executor, httpClient);
        loader.start("https://github.com/frido/flowable", consumer);
        assertEquals(
                "Document{{owner={login=frido}, name=flowable, createdAt=2017-05-06T09:52:29Z, description=null, homepageUrl=null, pushedAt=2017-05-06T09:52:29Z, stargazers={totalCount=0}, watchers={totalCount=0}, forks={totalCount=0}}}",
                consumer.getResponse().toString());
    }

    @Test
    public void testMongoQuery(){
        MongoDatabase db = new MongoDatabase();
        List<Document> list = new LinkedList<Document>();
        Set<String> set = new HashSet<>();
        db.getGitHubRelated().forEach((Document doc) -> {
            list.add(doc);
            set.add(doc.getString("Url"));
        });
        assertNotEquals(0, list.size());
        assertEquals(list.size(), set.size());
    }
}
