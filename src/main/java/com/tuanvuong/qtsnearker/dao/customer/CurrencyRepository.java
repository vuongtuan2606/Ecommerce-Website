package com.tuanvuong.qtsnearker.dao.customer;

import com.tuanvuong.qtsnearker.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

}
