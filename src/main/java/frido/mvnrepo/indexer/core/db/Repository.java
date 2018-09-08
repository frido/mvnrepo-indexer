package frido.mvnrepo.indexer.core.db;

public interface Repository<S> {
	public void save(S record);

	public void update(S record);

	public Iterable<S> getAll();
}