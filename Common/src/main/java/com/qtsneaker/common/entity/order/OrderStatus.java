package com.qtsneaker.common.entity.order;

public enum OrderStatus {

	NEW("Chờ xác nhận ") {
		@Override
		public String defaultDescription() {
			return "Đơn hàng đã được khách hàng đặt";
		}
	},

	CANCELLED("Đã hủy") {
		@Override
		public String defaultDescription() {
			return "Đơn đặt hàng đã bị từ chối";
		}
	},

	PROCESSING("Đang xử lý") {
		@Override
		public String defaultDescription() {
			return "Yêu cầu đang được xử lý";
		}
	},

	PACKAGED("Đã đóng gói") {
		@Override
		public String defaultDescription() {
			return "Sản phẩm đã được đóng gói";
		}
	},

	SHIPPING("Đang giao hàng") {
		@Override
		public String defaultDescription() {
			return "Shipper đang giao hàng";
		}
	},

	DELIVERED("Đã giao hàng") {
		@Override
		public String defaultDescription() {
			return "Khách hàng đã nhận được sản phẩm";
		}
	},;

	private final String vietnameseName;

	OrderStatus(String vietnameseName) {
		this.vietnameseName = vietnameseName;
	}

	public String getVietnameseName() {
		return vietnameseName;
	}

	public abstract String defaultDescription();
}
