package frido.mvnrepo.indexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubTest {

    Logger log = LoggerFactory.getLogger(GitHubTest.class);

    @Test
    public void testDownloader() {
        DummyGitHubHandler consumer = new DummyGitHubHandler();
        Executor executor = new NoThreadExecutor();
        Client httpClient = new GitHubClient(new JerseyHttpClient("frido", System.getenv().get("GITHUB_KEY")));
        //GitHubLoader loader = new GitHubLoader(executor, consumer, httpClient);
        Downloader loader = new Downloader(executor, httpClient, consumer);
        loader.start("https://github.com/frido/mvnrepo");
        assertEquals(
                "Document{{owner={login=frido}, name=mvnrepo, createdAt=2017-04-13T08:04:12Z, description=null, homepageUrl=null, pushedAt=2018-03-23T09:06:59Z, stargazers={totalCount=1}, watchers={totalCount=1}, forks={totalCount=0}}}",
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
        System.out.println("list.size:" + list.size());
        assertNotEquals(0, list.size());
        assertEquals(list.size(), set.size());
    }
}
