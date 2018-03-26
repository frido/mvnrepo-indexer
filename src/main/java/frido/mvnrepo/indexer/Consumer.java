package frido.mvnrepo.indexer;

interface Consumer{
    public void notify(String content);
    public void error(Throwable e);
}