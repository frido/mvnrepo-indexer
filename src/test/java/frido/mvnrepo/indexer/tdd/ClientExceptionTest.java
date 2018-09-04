package frido.mvnrepo.indexer.tdd;

import static org.junit.Assert.*;

import org.junit.Test;

import frido.mvnrepo.indexer.core.client.ClientException;

public class ClientExceptionTest {

	@Test
	public void getMessage() {
		ClientException ex = new ClientException("aaa", "bbb", 200, "ccc");
		assertTrue(ex.getMessage().contains("aaa"));
		assertTrue(ex.getMessage().contains("bbb"));
		assertTrue(ex.getMessage().contains("200"));
		assertTrue(ex.getMessage().contains("ccc"));
	}

}
