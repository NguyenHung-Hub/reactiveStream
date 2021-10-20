package dao;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoDatabase;

public class AbstractDao {
	private static final String DB_NAME = "nguyenhung";
	private MongoClient client;
	protected MongoDatabase db;

	public AbstractDao(MongoClient client) {
		super();
		this.client = client;
		this.db = client.getDatabase(DB_NAME);
	}
	
	public MongoClient getClient() {
		return client;
	}
	
	public MongoDatabase getDB() {
		return db;
	}
	
	
}
