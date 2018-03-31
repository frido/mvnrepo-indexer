package frido.mvnrepo.indexer;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.MockDatabase;
import frido.mvnrepo.indexer.core.NoThreadExecutor;
import frido.mvnrepo.indexer.core.db.MongoDatabase;
import frido.mvnrepo.indexer.github.GitHubProcessor;

public class GitHubProcessorTest {

    Logger log = LoggerFactory.getLogger(GitHubProcessorTest.class);

    @Test
    public void testDownloader() {
        MockDatabase database = new MockDatabase();
        ExecutorService executor = new NoThreadExecutor();
        GitHubProcessor process3 = new GitHubProcessor(database, executor);
        process3.start();
        assertEquals(
                "Document{{owner={login=frido}, name=flowable, createdAt=2017-05-06T09:52:29Z, description=null, homepageUrl=null, pushedAt=2017-05-06T09:52:29Z, stargazers={totalCount=0}, watchers={totalCount=0}, forks={totalCount=0}}}",
                database.getUpdated().get(0).toString());
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
