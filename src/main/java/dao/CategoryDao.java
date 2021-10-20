package dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import entity.Category;

public class CategoryDao extends AbstractDao {
	private MongoCollection<Document> categoryCollection;

	public CategoryDao(MongoClient client) {
		super(client);
		categoryCollection = db.getCollection("categories");
	}

	public Map<Category, Integer> getNumberItemsByCategory() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		Map<Category, Integer> numberItems = new HashMap<Category, Integer>();

		categoryCollection
				.aggregate(Arrays.asList(
						Document.parse(
								"{$lookup:{from:'items', localField:'_id', foreignField:'categoryID', as:'rs'}}"),
						Document.parse("{$project:{_id:1, name:1, description:1,\r\n" + " number:{$size:'$rs'}}}")))
				.subscribe(new Subscriber<Document>() {
					private Subscription subscription;
					@Override
					public void onSubscribe(Subscription s) {
						// TODO Auto-generated method stub
						subscription = s;
						subscription.request(1);

					}

					@Override
					public void onNext(Document t) {
						Category category = new Category(t.getString("_id"), t.getString("name"), t.getString("description"));
						int number = t.getInteger("number");
						
						numberItems.put(category, number);
						
						subscription.request(1);
					}

					@Override
					public void onError(Throwable t) {
						t.printStackTrace();
						onComplete();

					}

					@Override
					public void onComplete() {
						latch.countDown();
					}
				});
		
		latch.await();

		return numberItems;
	}
}
