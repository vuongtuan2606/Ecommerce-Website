package com.qtsneaker.ShopBackEnd.dao;


import com.qtsneaker.common.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminOrderRepository  extends JpaRepository<Order,Integer>, PagingAndSortingRepository<Order,Integer> {
    @Query("SELECT o FROM Order o " +
            "WHERE CONCAT('#', o.id) LIKE %?1% OR " +
            "      CONCAT(o.customer.firstName, ' ', o.customer.lastName) LIKE %?1% ")
    public Page<Order> findAll(String keyword, Pageable pageable);



    public Long countById(Integer id);
}
