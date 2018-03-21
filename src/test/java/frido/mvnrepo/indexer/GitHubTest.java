package frido.mvnrepo.indexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Executor executor = new DummyExecutor();
        HttpClient httpClient = new JerseyHttpClient("frido", System.getenv().get("GITHUB_KEY") );   
        GitHubLoader loader = new GitHubLoader(executor, consumer, httpClient);
        loader.start("https://github.com/frido/mvnrepo");
        assertEquals("Document{{createdAt=2017-04-13T08:04:12Z, description=null, homepageUrl=null, pushedAt=2018-03-19T10:01:26Z, stargazers={totalCount=1}, watchers={totalCount=1}, forks={totalCount=0}}}", consumer.getResponse().toString());
    }

    @Test
    public void testGitHubLink(){
        GitHubLink link = new GitHubLink("https://github.com/frido/mvnrepo");
        assertEquals("frido", link.getOwner());
        assertEquals("mvnrepo", link.getRepo());
    }
}
