package frido.mvnrepo.indexer;

public class PomUrlException extends Exception {
    private static final long serialVersionUID = 1L;

	public PomUrlException(String msg){
        super(msg);
    }
}