package model;

import java.time.LocalTime;

import javafx.beans.property.*;

public class CartProduct extends Product {
	private final ObjectProperty<LocalTime> timeOrdered = new SimpleObjectProperty<LocalTime>();
	private final ObjectProperty<LocalTime> timeCompleted = new SimpleObjectProperty<LocalTime>();
	private final IntegerProperty amount = new SimpleIntegerProperty(1);
	private final IntegerProperty maxAmount = new SimpleIntegerProperty(20);
	private final IntegerProperty totalPrice = new SimpleIntegerProperty(0);
	private final StringProperty tableNumber = new SimpleStringProperty("");
	private boolean orderSent = false;
	private boolean productReady = false;
	private boolean productDelivered = false;
	private final StringProperty status = new SimpleStringProperty("");

	public CartProduct(Product p, String tableNumber) {
		setProductName(p.toString());
		setPrice(p.getPrice());
		totalPrice.set(price.get());
		setTableNumber(tableNumber);
	}

	public void lowerProductAmount() {
		int newAmount = amount.get() - 1;
		setAmountAndPrice(newAmount);
	}

	public void setAmountAndPrice(int i) {
		amount.set(i);
		totalPrice.set(price.get() * amount.get());
	}

	public void setTableNumber(String s) {
		tableNumber.set(s);
	}
	
	public void sendOrder() {
		orderSent = true;
		timeOrdered.set(LocalTime.now());
		checkStatus();
	}
	
	public void productReady() {
		productReady = true;
		timeCompleted.set(LocalTime.now());
		checkStatus();
	}
	
	public void productDelivered() {
		productDelivered = true;
		checkStatus();
	}

	public void checkStatus() {
		if (orderSent) {
			if (productReady) {
				if (productDelivered) {
					status.set("Delivered");
				} else {
					status.set("Ready");
				}
			} else {
				status.set("Waiting");
			}
		}
	}

	public int getAmount() {
		return amount.get();
	}

	public int getMaxAmount() {
		return maxAmount.get();
	}

	public int getTotalPrice() {
		return totalPrice.get();
	}

	public String getTableNumber() {
		return tableNumber.get();
	}
	
	public boolean getOrderSentBoolean() {
		return orderSent;
	}
	
	public boolean getProductReadyBoolean() {
		return productReady;
	}
	
	public boolean getProductDeliveredBoolean() {
		return productDelivered;
	}
	
	public ObjectProperty<LocalTime> timeOrderedProperty() {
		return timeOrdered;
	}

	public ObjectProperty<LocalTime> timeCompletedProperty() {
		return timeCompleted;
	}
	
	public IntegerProperty amountProperty() {
		return amount;
	}

	public IntegerProperty maxAmountProperty() {
		return maxAmount;
	}

	public IntegerProperty totalPriceProperty() {
		return totalPrice;
	}

	public StringProperty tableNumberProperty() {
		return tableNumber;
	}

	public StringProperty statusProperty() {
		return status;
	}
}
