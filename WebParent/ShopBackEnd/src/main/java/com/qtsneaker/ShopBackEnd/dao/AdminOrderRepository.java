package com.qtsneaker.ShopBackEnd.dao;


import com.qtsneaker.common.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AdminOrderRepository  extends JpaRepository<Order,Integer>, PagingAndSortingRepository<Order,Integer> {
    @Query("SELECT o FROM Order o " +
            "WHERE CONCAT('#', o.id) LIKE %?1% OR " +
            "      CONCAT(o.customer.firstName, ' ', o.customer.lastName) LIKE %?1% ")
    public Page<Order> findAll(String keyword, Pageable pageable);

    @Query("SELECT o FROM Order o ORDER BY o.orderTime desc ")
    public List<Order> findTop6(Pageable pageable);

    public Long countById(Integer id);

    @Query("SELECT NEW Order (o.id, o.orderTime, o.productCost,"
            + " o.total) FROM Order o WHERE o.orderTime BETWEEN ?1 AND ?2 "
            +"AND  o.status = 'DELIVERED'"
            +"ORDER BY o.orderTime ASC "
            )
    public List<Order> findByOrderTimeBetween(Date startTime, Date endTime);

    @Query("SELECT COUNT(c) FROM Order c WHERE DAY(c.orderTime) = DAY(:date)")
    public Long countOrderByDay(@Param("date") Date date);

    @Query("SELECT COUNT(c) FROM Order c WHERE MONTH(c.orderTime) = MONTH(:date)")
    public Long countOrderByMonth(@Param("date") Date date);

    @Query("SELECT COUNT(c) FROM Order c WHERE YEAR(c.orderTime) = YEAR(:date)")
    public Long countOrderByYear(@Param("date") Date date);


    @Query("SELECT SUM(c.total) FROM Order c WHERE FUNCTION('DATE', c.orderTime) = FUNCTION('DATE', :date) AND c.status = 'DELIVERED'")
    public Long sumTotalToday(@Param("date") Date date);

    @Query("SELECT SUM(c.total) FROM Order c WHERE c.orderTime >= :startDate AND c.orderTime < :endDate AND c.status = 'DELIVERED'")
    public Long sumTotalLast7Days(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT SUM(c.total) FROM Order c WHERE c.orderTime >= :startDate AND c.orderTime < :endDate AND c.status = 'DELIVERED'")
    public Long sumTotalLastMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
