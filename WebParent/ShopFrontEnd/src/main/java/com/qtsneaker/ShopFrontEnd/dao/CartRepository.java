package com.qtsneaker.ShopFrontEnd.dao;

import com.qtsneaker.common.entity.Cart;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CartRepository extends JpaRepository<Cart, Integer> {
	public List<Cart> findByCustomer(Customer customer);

	public Cart findByCustomerAndProduct(Customer customer, Product product);
	
	@Modifying
	@Query("UPDATE Cart c SET c.quantity = ?1 WHERE c.customer.id = ?2 AND c.product.id = ?3")
	public void updateQuantity(Integer quantity, Integer customerId, Integer productId);

	@Modifying
	@Query("DELETE FROM Cart c WHERE c.customer.id = ?1 AND c.product.id = ?2")
	public void deleteByCustomerAndProduct(Integer customerId, Integer productId);

	@Modifying
	@Query("DELETE Cart c WHERE c.customer.id = ?1")
	public void deleteByCustomer(Integer customerId);
}
