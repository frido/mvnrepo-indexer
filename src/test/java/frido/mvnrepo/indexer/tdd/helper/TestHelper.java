package frido.mvnrepo.indexer.tdd.helper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class TestHelper {

	private static String path = null;

	public static String readFile(String name) throws URISyntaxException, IOException {
		URL x = new TestHelper().getClass().getResource("../../../../../");
		System.out.println(".................:" + x.toString());
		URI file = new TestHelper().getClass().getResource("../../../../../" + name).toURI();
		byte[] content = Files.readAllBytes(Paths.get(file));
		String text = new String(content, Charset.forName("utf-8"));
		return text;
	}

	public static Document readJson(String name) throws URISyntaxException, IOException {
		BasicDBObject doc = (BasicDBObject) JSON.parse(readFile(name));
		Document xxx = new Document(doc);
		return xxx;
	}

}
