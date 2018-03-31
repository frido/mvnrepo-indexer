package frido.mvnrepo.indexer.core.download;

public interface Prioritable extends Runnable{
    public int getPriority();
}