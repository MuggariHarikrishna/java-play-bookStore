package dao;

import com.mongodb.MongoClient;
import com.typesafe.config.ConfigFactory;

public class MongoConnectionSingleton {
    private static String host = ConfigFactory.load().getString("mongo.host");
    private static int port = Integer.parseInt(ConfigFactory.load().getString("mongo.port"));
    private static MongoClient mongoClient = new MongoClient(host, port);

    public static MongoClient getConnection() {
        return mongoClient;
    }
}
