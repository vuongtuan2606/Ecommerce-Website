package com.qtsneaker.ShopBackEnd.dao;


import com.qtsneaker.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AdminCustomerRepository extends JpaRepository<Customer, Integer>, PagingAndSortingRepository<Customer,Integer> {
	
	@Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ', c.lastName, ' ', "
			+ " c.phoneNumber) LIKE %?1%")
	public Page<Customer> findAll(String keyword, Pageable pageable);
	
	@Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);

	public Long countById(Integer id);

	@Query("SELECT COUNT(c) FROM Customer c WHERE DAY(c.createdTime) = DAY(:date)")
	public Long countCustomersByDay(@Param("date") Date date);

	@Query("SELECT COUNT(c) FROM Customer c WHERE MONTH(c.createdTime) = MONTH(:date)")
	public Long countCustomersByMonth(@Param("date") Date date);

	@Query("SELECT COUNT(c) FROM Customer c WHERE YEAR(c.createdTime) = YEAR(:date)")
	public Long countCustomersByYear(@Param("date") Date date);

}
