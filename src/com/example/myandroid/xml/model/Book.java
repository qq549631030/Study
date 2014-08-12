package com.example.myandroid.xml.model;

public class Book {

	public static final String ELEMENT = "book";
	public static final String ELEMENT_ID = "id";
	public static final String ELEMENT_NAME = "name";
	public static final String ELEMENT_PRICE = "price";

	private int id;
	private String name;
	private float price;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", price=" + price + "]";
	}

}
