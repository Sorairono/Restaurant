package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Table {
	private IntegerProperty tableNumber = new SimpleIntegerProperty(0);
	private StringProperty zoneLetter = new SimpleStringProperty("");
	private BooleanProperty isCircle = new SimpleBooleanProperty(false);
	private StringProperty property = new SimpleStringProperty("Rectangle");
	private ObservableList<CartProduct> customerProduct = FXCollections.observableArrayList();
	private Coupon coupon = new Coupon();
	private final double taxRate = 0.1;
	private IntegerProperty subTotalProperty = new SimpleIntegerProperty(0);
	private IntegerProperty taxProperty = new SimpleIntegerProperty(0);
	private IntegerProperty totalProperty = new SimpleIntegerProperty(0);
	private IntegerProperty discountProperty = new SimpleIntegerProperty(0);
	private IntegerProperty balanceProperty = new SimpleIntegerProperty(0);
	private BooleanProperty occupied = new SimpleBooleanProperty(false);
	private BooleanProperty foodReady = new SimpleBooleanProperty(false);
	private BooleanProperty merge = new SimpleBooleanProperty(false);
	private StringProperty status = new SimpleStringProperty("Empty");
	private StringProperty mergeTable = new SimpleStringProperty("");
	private IntegerProperty capacity = new SimpleIntegerProperty(0);
	
	public Table(String zoneLetter, int tableNumber, int capacity, boolean circle) {
		setTableNumber(tableNumber);
		setZoneLetter(zoneLetter);
		setCapacity(capacity);
		setProperty(circle);
		occupied.addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				if (!occupied.get() && !merge.get()) {
					status.set("Empty");
				} else {
					status.set("Occupied");
				}
			}
		});
		merge.addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				if (!occupied.get() && !merge.get()) {
					status.set("Empty");
				} else {
					status.set("Occupied");
				}
			}
		});
	}

	public void setTableNumber(int i) {
		tableNumber.set(i);
	}
	
	public void setZoneLetter(String s) {
		zoneLetter.set(s);
	}
	
	public void setCapacity(int i) {
		capacity.set(i);
	}
	
	public void setProperty(boolean b) {
		if (b == true) {
			isCircle.set(true);
			property.set("Circle");
		}
	}
	
	public void setTable(Table table) {
		customerProduct = FXCollections.observableArrayList(table.getCustomerProduct());
		setCoupon(table.getCoupon());
		setBill();
		setOccupied(table.getOccupied());
		setFoodReady(table.getFoodReady());
	}

	public void clearTable() {
		customerProduct.clear();
		setBill();
		setCoupon(new Coupon());
		setOccupied(false);
		setMergeProperty(false);
	}

	public void setCoupon(Coupon c) {
		coupon = c;
		setBill();
	}

	public void setBill() {
		int sub_total = 0;
		for (int i = 0; i < customerProduct.size(); i++) {
			sub_total += customerProduct.get(i).getTotalPrice();
		}
		int tax = (int) (sub_total * taxRate);
		int total = sub_total + tax;
		int discount;
		if (coupon.getDiscountValue() > 0) {
			if (coupon.getPercentBoolean()) {
				discount = (int) (total * (1 - coupon.getDiscountValue()));
			} else {
				discount = (int) (coupon.getDiscountValue());
			}
		} else {
			discount = 0;
		}
		int balance = total - discount;
		subTotalProperty.set(sub_total);
		taxProperty.set(tax);
		totalProperty.set(total);
		discountProperty.set(discount);
		balanceProperty.set(balance);
	}

	public void setOccupied(boolean b) {
		occupied.set(b);
	}

	public void setFoodReady(boolean b) {
		foodReady.set(b);
	}
	
	public void setMergeProperty(boolean b) {
		merge.set(b);
	}
	
	public void setMergeTable(String s) {
		mergeTable.set(s);
	}

	public int getTableNumber() {
		return tableNumber.get();
	}
	
	public String getZoneLetter() {
		return zoneLetter.get();
	}
	
	@Override
	public String toString() {
		return zoneLetter.get() + String.valueOf(tableNumber.get());
	}
	
	public boolean isCircle() {
		return isCircle.get();
	}

	public ObservableList<CartProduct> getCustomerProduct() {
		return customerProduct;
	}

	public int getSubtotal() {
		return subTotalProperty.get();
	}

	public int getTax() {
		return taxProperty.get();
	}

	public int getTotal() {
		return totalProperty.get();
	}

	public int getDiscount() {
		return discountProperty.get();
	}

	public int getBalance() {
		return balanceProperty.get();
	}

	public boolean getOccupied() {
		return occupied.get();
	}

	public boolean getFoodReady() {
		return foodReady.get();
	}
	
	public boolean getMergeBoolean() {
		return merge.get();
	}
	
	public String getMergeTable() {
		return mergeTable.get();
	}
	
	public int getCapacity() {
		return capacity.get();
	}
	
	public String getStatus() {
		return status.get();
	}
	
	public BooleanProperty occupiedProperty() {
		return occupied;
	}
	
	public BooleanProperty foodReadyProperty() {
		return foodReady;
	}
	
	public BooleanProperty mergeProperty() {
		return merge;
	}
	
	public IntegerProperty tableNumberProperty() {
		return tableNumber;
	}
	
	public IntegerProperty capacityProperty() {
		return capacity;
	}
	
	public StringProperty propertyProperty() {
		return property;
	}
	
	public StringProperty statusProperty() {
		return status;
	}
	
	public Coupon getCoupon() {
		return coupon;
	}
}
