package com.qtsneaker.ShopBackEnd.dao;


import com.qtsneaker.common.entity.order.OrderDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Date;
import java.util.List;

public interface AdminOrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
	
	@Query("SELECT NEW OrderDetail (d.product.category.name, d.quantity,"
			+ " d.productCost, d.subtotal)"
			+ " FROM OrderDetail d "
			+ " JOIN d.order o "
			+ " WHERE d.order.orderTime BETWEEN ?1 AND ?2"
			+ " AND o.status ='DELIVERED' ")
	public List<OrderDetail> findWithCategoryAndTimeBetween(Date startTime, Date endTime);
	
	@Query("SELECT NEW OrderDetail (d.quantity, d.product.name,"
			+ " d.productCost, d.subtotal)"
			+ " FROM OrderDetail d"
			+ " JOIN d.order o "
			+ " WHERE d.order.orderTime BETWEEN ?1 AND ?2"
			+ " AND o.status ='DELIVERED'")
	public List<OrderDetail> findWithProductAndTimeBetween(Date startTime, Date endTime);

	@Query("SELECT od FROM OrderDetail od "
			+ "JOIN od.order o "
			+ "WHERE o.status = 'DELIVERED' "
			+ "AND o.orderTime BETWEEN ?1 AND ?2 "
			+ "ORDER BY od.quantity DESC")
	List<OrderDetail> findOrderDetailsByOrderTimeBetween(Date startDate, Date endDate, Pageable pageable);



}
