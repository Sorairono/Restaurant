package model;

import javafx.beans.property.*;

public class Product {
	protected final StringProperty productName = new SimpleStringProperty("");
	protected final IntegerProperty price = new SimpleIntegerProperty(0);
	private int category;
	private int type;

	public Product() {
		this("", 0, 0, 0);
	}

	public Product(String productName, int price, int category, int type) {
		setProductName(productName);
		setPrice(price);
		setCategory(category);
		setType(type);
	}

	public void setProductName(String s) {
		productName.set(s);
	}

	public void setPrice(int i) {
		price.set(i);
	}

	public void setCategory(int i) {
		category = i;
	}

	public void setType(int i) {
		type = i;
	}

	@Override
	public String toString() {
		return productName.get();
	}

	public int getPrice() {
		return price.get();
	}

	public int getCategory() {
		return category;
	}

	public int getType() {
		return type;
	}

	public StringProperty productNameProperty() {
		return productName;
	}

	public IntegerProperty priceProperty() {
		return price;
	}
}
