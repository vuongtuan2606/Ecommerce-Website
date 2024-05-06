package com.qtsneaker.ShopFrontEnd.dao;


import com.qtsneaker.common.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

}
