package com.qtsneaker.ShopFrontEnd.services.Checkout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CheckoutInfo {
	private float productTotal;
	private float shippingCostTotal;
	private float paymentTotal;
	public float getProductTotal() {
		return productTotal;
	}

	public void setProductTotal(float productTotal) {
		this.productTotal = productTotal;
	}

	public float getShippingCostTotal() {
		return shippingCostTotal;
	}

	public void setShippingCostTotal(float shippingCostTotal) {
		this.shippingCostTotal = shippingCostTotal;
	}

	public float getPaymentTotal() {
		return paymentTotal;
	}

	public void setPaymentTotal(float paymentTotal) {
		this.paymentTotal = paymentTotal;
	}
}
