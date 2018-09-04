package frido.mvnrepo.indexer.tdd;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import frido.mvnrepo.indexer.pom.PomLinkBuilder;
import frido.mvnrepo.indexer.pom.PomUrlException;

public class PomLinkBuilderTest {

	@Test
	public void build() throws PomUrlException {
		PomLinkBuilder builder = new PomLinkBuilder();
		builder.repo("http://central.maven.org/maven2/").group("GROUP").artifact("ARTIFACT").version("VERSION");
		assertEquals("http://central.maven.org/maven2/GROUP/ARTIFACT/VERSION/ARTIFACT-VERSION.pom", builder.build());
	}

}
