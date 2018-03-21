package frido.mvnrepo.indexer;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadTask implements Runnable {

    Logger log = LoggerFactory.getLogger(DownloadTask.class);

    private Document metadata;
    private Downloader ctx;

    public DownloadTask(Downloader ctx, Document metadata) {
        this.metadata = metadata;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        try {
            String link = generateLink(this.metadata);
            String content = this.ctx.download(link);
            this.ctx.notify(content);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    // TODO: existuju duplicitne metadata vid http://central.maven.org/maven2/antlr/antlr/
    // TODO: najst sposob ako ich ignorovat
    private String generateLink(Document doc) throws Exception { //TODO: input groupId, arch... instead of Document
        String groupId = doc.getString("groupId");
        String artifactId = doc.getString("artifactId");
        String version = doc.getString("version");
        if (groupId == null || artifactId == null || version == null) {
            throw new Exception("Wrong metadata: " + doc);
        }
        // TODO: base url configurable
        return "http://central.maven.org/maven2/" + groupId.replace(".", "/") + "/" + artifactId + "/" + version + "/"
                + artifactId + "-" + version + ".pom";
    }

}