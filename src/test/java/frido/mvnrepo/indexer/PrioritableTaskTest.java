package frido.mvnrepo.indexer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import frido.mvnrepo.indexer.core.download.PrioritableTask;

public class PrioritableTaskTest {
    @Test
    public void testTaskPriority() {
        PrioritableTask task = new PrioritableTask("http://central.maven.org/maven2/activemq/activemq/", null,
                null);
        assertEquals(6, task.getPriority());
    }
}