package entity;

import org.bson.codecs.pojo.annotations.BsonId;

public class Category {
	@BsonId
	private String id;
	private String name;
	private String description;

	public Category() {
		super();
	}

	public Category(String id) {
		super();
		this.id = id;
	}

	public Category(String id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

}
