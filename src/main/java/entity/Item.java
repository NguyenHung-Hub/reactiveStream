package entity;

import java.util.List;

import org.bson.codecs.pojo.annotations.BsonId;

public class Item {
	@BsonId
	private String id;
	private String description;
	private String manufacture;
	private String strapType;
	private double price;
	private List<String> measuringModels;
	private String size;

	private Battery battery;
	private Category category;

	public Item() {
		super();
	}

	public Item(String id, String description, String manufacture, String strapType, double price,
			List<String> measuringModels, String size) {
		super();
		this.id = id;
		this.description = description;
		this.manufacture = manufacture;
		this.strapType = strapType;
		this.price = price;
		this.measuringModels = measuringModels;
		this.size = size;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	public String getStrapType() {
		return strapType;
	}

	public void setStrapType(String strapType) {
		this.strapType = strapType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public List<String> getMeasuringModels() {
		return measuringModels;
	}

	public void setMeasuringModels(List<String> measuringModels) {
		this.measuringModels = measuringModels;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Battery getBattery() {
		return battery;
	}

	public void setBattery(Battery battery) {
		this.battery = battery;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", description=" + description + ", manufacture=" + manufacture + ", strapType="
				+ strapType + ", price=" + price + ", measuringModels=" + measuringModels + ", size=" + size
				+ ", battery=" + battery + ", category=" + category + "]";
	}

}
