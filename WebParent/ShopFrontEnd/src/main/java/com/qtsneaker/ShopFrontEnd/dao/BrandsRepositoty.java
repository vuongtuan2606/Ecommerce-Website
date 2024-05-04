package com.qtsneaker.ShopFrontEnd.dao;

import com.qtsneaker.common.entity.Brand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;



public interface BrandsRepositoty extends JpaRepository<Brand,Integer>, PagingAndSortingRepository<Brand,Integer> {

}
