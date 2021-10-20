package app;

import java.awt.event.FocusEvent.Cause;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import dao.CategoryDao;
import dao.ItemDao;
import entity.Battery;
import entity.Category;
import entity.Item;

public class App {
	public static void main(String[] args) throws InterruptedException {

		MongoClient client = MongoClients.create();
		ItemDao itemDao = new ItemDao(client);
		CategoryDao categoryDao = new CategoryDao(client);
//		Cau 2b
//		Item item2 = new Item("9999", "abc", "abc", "abc", 1000, Arrays.asList("def"), "30");
//		Category category2 = new Category("8888", "jkl", "iop");
//		item2.setCategory(category2);
//
//		Battery battery2 = new Battery("12", "tech new", "123", "uio");
//		item2.setBattery(battery2);
//
//		boolean rs2 = itemDao.addItem(item2);
//		if (rs2 == true) {
//			System.out.println("chen thanh cong");
//		} else {
//			System.out.println("that bai");
//		}

//		Cau 2a

//		List<Item> list = itemDao.getItemsByPrice(60, 1000);
//		list.forEach(i -> {
//			System.out.println(i);
//		});

//		Cau 2c
//		categoryDao.getNumberItemsByCategory().entrySet().iterator().forEachRemaining(i->{
//			System.out.print(i.getKey() + " ");
//			System.out.println(i.getValue());
//		});

//		 Cau 2d
//		List<Item> item4 = itemDao.getItems("mode");
//		item4.forEach(i->{
//			System.out.println(i);
//		});

//		Cau 2e
//		Battery battery = new Battery("new voltage", "new technology", "new serial", "manufacture");
//
//		boolean update = itemDao.updateManufactureBattery("9999", battery);
//		if (update == true) {
//			System.out.println("cap nhat thanh cong");
//		} else {
//			System.out.println("cap nhat thast bai");
//		}

//		Cau 2f
		itemDao.getItemsByManufacturer().forEach(i -> {
			System.out.println(i);
		});
	}
}
