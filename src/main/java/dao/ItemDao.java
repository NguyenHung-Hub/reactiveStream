package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bson.Document;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import entity.Battery;
import entity.Category;
import entity.Item;

public class ItemDao extends AbstractDao {

	private static final Gson GSON = new Gson();
	private MongoCollection<Document> itemCollection;

	public ItemDao(MongoClient client) {
		super(client);
		itemCollection = db.getCollection("items");
	}

	public boolean addItem(Item item) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		AtomicBoolean rs = new AtomicBoolean(false);

		String json = GSON.toJson(item);
		Document document = Document.parse(json);

		Document categoryDoc = (Document) document.get("category");
//		System.out.println("cateDoc " + categoryDoc);
		document.append("_id", document.getString("id"));
//		System.out.println("\n" + document);
		document.remove("id");
//		System.out.println("\n" + document);
		document.remove("category");
//		System.out.println("\n" + document);
		document.append("categoryID", categoryDoc.getString("id"));
//		System.out.println("\n" + document);

		Publisher<InsertOneResult> publisher = itemCollection.insertOne(document);
		Subscriber<InsertOneResult> subscriber = new Subscriber<InsertOneResult>() {

			public void onSubscribe(Subscription s) {
				s.request(1);
			}

			public void onNext(InsertOneResult t) {
				if (t.getInsertedId() != null) {
					rs.set(true);
				}
			}

			public void onError(Throwable t) {
				t.printStackTrace();
				onComplete();

			}

			public void onComplete() {
				latch.countDown();
			}
		};

		publisher.subscribe(subscriber);
		latch.await();
		return rs.get();
	}

	public List<Item> getItemsByPrice(double from, double to) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		List<Item> items = new ArrayList<Item>();
		Document document = Document.parse("{$and:[{price:{$gte:" + from + "}},{price:{$lte:" + to + "}}]}");

		itemCollection.find(document).subscribe(new Subscriber<Document>() {

			private Subscription subscription;

			@Override
			public void onSubscribe(Subscription s) {
				subscription = s;
				subscription.request(1);

			}

			@Override
			public void onNext(Document t) {
				String json = t.toJson();

				Category category = new Category(t.getString("categoryID"));
				Item item = GSON.fromJson(json, Item.class);
				item.setId(t.getString("_id"));
				item.setCategory(category);

				items.add(item);

				subscription.request(1);
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				t.printStackTrace();
				onComplete();
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub

				latch.countDown();
			}
		});

		latch.await();
		return items;

	}

	public List<Item> getItems(String keyWord) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		List<Item> items = new ArrayList();

		Document document = Document.parse("{$text:{$search:'" + keyWord + "'}}");

		itemCollection.find(document).subscribe(new Subscriber<Document>() {
			private Subscription subscription;

			@Override
			public void onSubscribe(Subscription s) {
				subscription = s;
				subscription.request(1);
			}

			@Override
			public void onNext(Document t) {
				String json = t.toJson();

				Category category = new Category(t.getString("categoryID"));
				Item item = GSON.fromJson(json, Item.class);
				item.setId(t.getString("_id"));
				item.setCategory(category);

				items.add(item);

				subscription.request(1);

			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				t.printStackTrace();
				onComplete();
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				latch.countDown();
			}
		});

		latch.await();

		return items;
	}

	public boolean updateManufactureBattery(String idItem, Battery newBattery) throws InterruptedException {

		CountDownLatch latch = new CountDownLatch(1);
		AtomicBoolean rs = new AtomicBoolean(false);

		String json = GSON.toJson(newBattery);
		Document document = Document.parse(json);

		itemCollection.updateOne(Filters.eq("_id", idItem), Updates.set("battery", document))
				.subscribe(new Subscriber<UpdateResult>() {

					@Override
					public void onSubscribe(Subscription s) {
						// TODO Auto-generated method stub
						s.request(1);

					}

					@Override
					public void onNext(UpdateResult t) {
						// TODO Auto-generated method stub
						if (t.getModifiedCount() > 0) {
							rs.set(true);

						}
					}

					@Override
					public void onError(Throwable t) {
						// TODO Auto-generated method stub
						t.printStackTrace();
						onComplete();
					}

					@Override
					public void onComplete() {
						// TODO Auto-generated method stub
						latch.countDown();
					}
				});
		latch.await();
		return rs.get();
	}

	public List<Item> getItemsByManufacturer() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		List<Item> items = new ArrayList();

		itemCollection.aggregate(Arrays.asList(Document.parse("{$match:{manufacturer:'Sevenfriday'}}"),
				Document.parse("{$group:{_id: null, item:{$addToSet:'$$ROOT'}, min:{$min:'$price'}}}"),
				Document.parse("{$unwind:'$item'}"), Document.parse("{$match:{$expr:{$eq:['$min','$item.price']}}}"),
				Document.parse("{$replaceWith:'$item'}")

		)).subscribe(new Subscriber<Document>() {
			private Subscription subscription;

			@Override
			public void onSubscribe(Subscription s) {
				// TODO Auto-generated method stub
				subscription = s;
				subscription.request(1);

			}

			@Override
			public void onNext(Document t) {
				String json = t.toJson();
				Category category = new Category(t.getString("categoryID"));
				Item item = GSON.fromJson(json, Item.class);
				item.setId(t.getString("_id"));
				item.setCategory(category);

				items.add(item);
				subscription.request(1);
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				t.printStackTrace();
				onComplete();

			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				latch.countDown();
			}
		});
		latch.await();
		return items;
	}

}
