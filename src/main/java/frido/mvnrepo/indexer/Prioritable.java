package frido.mvnrepo.indexer;

interface Prioritable extends Runnable{
    public int getPriority();
}