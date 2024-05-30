package com.qtsneaker.common.entity.order;


import com.qtsneaker.common.entity.Category;
import com.qtsneaker.common.entity.IdBasedEntity;
import com.qtsneaker.common.entity.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_order_details")
public class OrderDetail extends IdBasedEntity {
	private int quantity;
	@Column(name = "product_cost")
	private float productCost;
	private float unitPrice;
	private float subtotal;
	private  int size;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	public OrderDetail() {
	}
	public OrderDetail(String categoryName, int quantity, float productCost, float subtotal) {
		this.product = new Product();
		this.product.setCategory(new Category(categoryName));
		this.quantity = quantity;
		this.productCost = productCost ;
		this.subtotal = subtotal;
	}

	public OrderDetail(int quantity, String productName, float productCost,  float subtotal) {
		this.product = new Product(productName);
		this.quantity = quantity;
		this.productCost = productCost;
		this.subtotal = subtotal;
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
