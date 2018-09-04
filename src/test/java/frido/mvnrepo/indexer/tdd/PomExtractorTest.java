package frido.mvnrepo.indexer.tdd;

public class PomExtractorTest {

//	@Test
//	public void test() {
//		PomExtractorDatabase db = new PomExtractorDatabase();
//		DownloadExecutor executor = new DownloadExecutor(new NoThreadExecutor());
//		HttpClient client = new JerseyHttpClient(); // TODO: mock client
//		Producer producer = new PomLinkProducer(db);
//		Consumer consumer = new PomProcessor(db);
//		DownloadManager processor = new DownloadManager(producer, client, executor, consumer);
//		processor.start();
//		assertEquals(1, db.getUpdated().size());
//	}
//
//	class PomExtractorDatabase extends MockDatabase {
//
//		private List<Document> uptaded = new ArrayList<>();
//
//		@Override
//		public Iterable<Document> getAll(String collection) {
//			if (collection != "metadata") {
//				fail("Wrong collection");
//			}
//			try {
//				// TODO: to helper
//				URI file = this.getClass().getResource("../../../../metadata.json").toURI();
//				byte[] content = Files.readAllBytes(Paths.get(file));
//				String text = new String(content, Charset.forName("utf-8"));
//				BasicDBObject doc = (BasicDBObject) JSON.parse(text);
//				Document xxx = new Document(doc);
//				return Arrays.asList(xxx);
//			} catch (URISyntaxException | IOException e) {
//				fail("Fail to read file");
//			}
//			return null;
//
//		}
//
//		@Override
//		public void update(String collection, Document query, Document newOne) {
//			uptaded.add(newOne);
//		}
//
//		public List<Document> getUpdated() {
//			return uptaded;
//		}
//
//	}

}
