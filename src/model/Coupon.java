package model;

public class Coupon {
	private String couponCode;
	private boolean percentDiscount;
	private double discountAmount;

	public Coupon() {
		this("", false, 0);
	}

	public Coupon(String couponCode, boolean percentDiscount, double discountValue) {
		setCouponCode(couponCode);
		setPercentBoolean(percentDiscount);
		setDiscountValue(discountValue);
	}

	public void setCouponCode(String s) {
		couponCode = s;
	}

	public void setDiscountValue(double i) {
		discountAmount = i;
	}

	public void setPercentBoolean(boolean b) {
		percentDiscount = b;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public double getDiscountValue() {
		return discountAmount;
	}

	public boolean getPercentBoolean() {
		return percentDiscount;
	}
}
