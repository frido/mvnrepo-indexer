package frido.mvnrepo.indexer;

interface Consumer{
    public void notify(String url, String content);
    public void error(Throwable e);
}