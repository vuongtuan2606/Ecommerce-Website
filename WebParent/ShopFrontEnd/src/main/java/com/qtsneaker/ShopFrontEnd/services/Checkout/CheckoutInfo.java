package com.qtsneaker.ShopFrontEnd.services.Checkout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CheckoutInfo {
	private float productCost;
	private float productTotal;
	private float paymentTotal;
	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getProductTotal() {
		return productTotal;
	}

	public void setProductTotal(float productTotal) {
		this.productTotal = productTotal;
	}

	public float getPaymentTotal() {
		return paymentTotal;
	}

	public void setPaymentTotal(float paymentTotal) {
		this.paymentTotal = paymentTotal;
	}
}
