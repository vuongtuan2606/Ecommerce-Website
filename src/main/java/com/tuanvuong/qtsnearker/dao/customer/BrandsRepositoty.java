package com.tuanvuong.qtsnearker.dao.customer;

import com.tuanvuong.qtsnearker.entity.Brand;

import com.tuanvuong.qtsnearker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BrandsRepositoty extends JpaRepository<Brand,Integer>, PagingAndSortingRepository<Brand,Integer> {

}
