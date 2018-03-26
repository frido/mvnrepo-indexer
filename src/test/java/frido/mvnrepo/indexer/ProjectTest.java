package frido.mvnrepo.indexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Iterator;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectTest {

    Logger log = LoggerFactory.getLogger(ProjectTest.class);

    @Test
    public void getUniqFilter() throws Exception {
        Database database = new MongoDatabase();
        Iterator<Document> iterator = database.getAll("projects").iterator();
        if (iterator.hasNext()) {
            Document doc = iterator.next();
            Project project = new Project(doc);
            Iterator<Document> it = database.getByFilter("projects", project.getUniqFilter()).iterator();
            Document result = it.next();
            assertEquals(doc.getString("name"), result.getString("name"));
            assertFalse(it.hasNext());
        }
    }
}
