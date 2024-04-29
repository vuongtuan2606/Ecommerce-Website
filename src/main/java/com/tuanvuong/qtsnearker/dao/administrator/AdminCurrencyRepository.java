package com.tuanvuong.qtsnearker.dao.administrator;

import com.tuanvuong.qtsnearker.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AdminCurrencyRepository extends JpaRepository<Currency,Integer> {
    public List<Currency> findAllByOrderByNameAsc();
}
