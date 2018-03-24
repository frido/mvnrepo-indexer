package frido.mvnrepo.indexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: remove
public class GitHubLoaderTask implements Runnable {

    Logger log = LoggerFactory.getLogger(GitHubLoaderTask.class);

    private String gitHubLink;
    private GitHubLoader ctx;

    public GitHubLoaderTask(GitHubLoader ctx, String gitHubLink) {
        this.ctx = ctx;
        this.gitHubLink = gitHubLink;
    }

    @Override
    public void run() {
        try {
            String content = this.ctx.download(this.gitHubLink);
            this.ctx.notify(content);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

}