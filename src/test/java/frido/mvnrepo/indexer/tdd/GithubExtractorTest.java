package frido.mvnrepo.indexer.tdd;

public class GithubExtractorTest {

//	@Test
//	public void test() {
//		PomExtractorDatabase db = new PomExtractorDatabase();
//		DownloadExecutor executor = new DownloadExecutor(new NoThreadExecutor());
//		// TODO: mock client
//		HttpClient client = new GitHubProcessor(new JerseyHttpClient("frido", System.getenv().get("GITHUB_KEY"))); // TODO:
//		Producer producer = new GithubLinkProducer(db);
//		Consumer consumer = new GithubConsumer(db);
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
//		public Iterable<Document> findByUrlWithGithub() {
//			try {
//				return Arrays.asList(TestHelper.readJson("pom.json"));
//			} catch (URISyntaxException | IOException e) {
//				e.printStackTrace();
//			}
//			return null;
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
