package frido.mvnrepo.indexer.core.client;

public class ClientException extends Exception {
    private static final long serialVersionUID = -4396290349016890943L;

	public ClientException(String msg){
        super(msg);
    }

    public ClientException(Throwable t){
        super(t);
    }

    public ClientException(String url, String query, int status, String body) {
        this(String.format("HTTP ERROR:%s url=%s %n query=%s %n response=%s", status, url, query, body));
    }
}