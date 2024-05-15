package com.qtsneaker.common.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "tbl_cart_items")
public class Cart extends IdBasedEntity {
	private int quantity;

	public Cart() {
	}
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "product_size")
	private Integer productSize;
	public Integer getProductSize() {
		return productSize;
	}

	public void setProductSize(Integer productSize) {
		this.productSize = productSize;
	}
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Cart{" +
				"id=" + id +
				", customer=" + customer +
				", product=" + product +
				", quantity=" + quantity+
				'}';
	}
	@Transient
	public float getSubtotal() {
		return product.getDiscountPrice() * quantity;
	}


}
