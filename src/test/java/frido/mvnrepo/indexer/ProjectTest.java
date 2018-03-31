package frido.mvnrepo.indexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Iterator;

import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import frido.mvnrepo.indexer.core.db.Database;
import frido.mvnrepo.indexer.core.db.MongoDatabase;
import frido.mvnrepo.indexer.github.Project;

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

    @Test
    public void generalTest(){
        Iterable<String> iterable = Arrays.asList("a","b","c","d","e");
        int size1 = 0;
        int size2 = 0;
        Iterator<String> it1 = iterable.iterator();
        while(it1.hasNext()){
            it1.next();
            size1++;
        }
        Iterator<String> it2 = iterable.iterator();
        while(it2.hasNext()){
            it2.next();
            size2++;
        }
        assertEquals(5, size2);
        assertEquals(size1, size2);
    }
}
