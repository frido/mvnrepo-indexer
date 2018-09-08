package frido.mvnrepo.indexer.core.db;

public interface Converter<S, T> {

	T toEntity(S record);

	S toRecord(T doc);

}
