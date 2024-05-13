package com.qtsneaker.common.entity.order;

public enum PaymentMethod {
	COD("Thanh toán khi nhận hàng"),
	CREDIT_CARD("Thẻ tín dụng");

	private final String vietnameseName;

	PaymentMethod(String vietnameseName) {
		this.vietnameseName = vietnameseName;
	}

	public String getVietnameseName() {
		return vietnameseName;
	}
}

