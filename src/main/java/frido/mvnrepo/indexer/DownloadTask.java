package frido.mvnrepo.indexer;

// TODO: remove
@Deprecated
public class DownloadTask implements Runnable {
/*
    Logger log = LoggerFactory.getLogger(DownloadTask.class);

    private String link;
    private Downloader ctx;

    public DownloadTask(Downloader ctx, String link) {
        this.link = link;
        this.ctx = ctx;
    }
*/
    @Override
    public void run() {
        /*try {
            String content = this.ctx.download(this.link);
            this.ctx.notify(content);
        } catch (Exception e) {
            log.error("Downloader - Task - Error", e);
        }*/
    }
}