package com.qtsneaker.ShopFrontEnd.dao;

import com.qtsneaker.common.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province,Integer> {
    List<Province> findAllByOrderByNameAsc();
}
