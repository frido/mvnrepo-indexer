package frido.mvnrepo.indexer;

import java.util.Map;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App implements MatchHandler {
    
    private static final String ENV_MONGO_URL = "MONGO_URL";
    
    private Mongo mongo;

    //TODO: file properties, env, params?
    private Map<String, String> getProperties(){
        Map<String, String> env = System.getenv();
        return env;
    }

    public App(){
        String connectionString = getProperties().get(ENV_MONGO_URL);
        this.mongo = new Mongo(connectionString);
    }

    /** 
     * Run main application.
     */
    public static void main(String[] args) {
        App app = new App();
        Crawler crawler = new Crawler(app);
        crawler.search("http://central.maven.org/maven2/", 0);//TODO: configurable
        //TODO: remove 0 parameter
    }

    @Override
    public void match(String content){
       // System.out.println(content);
        this.mongo.save("metadata", Metadata.valueOf(content));
    }
}
