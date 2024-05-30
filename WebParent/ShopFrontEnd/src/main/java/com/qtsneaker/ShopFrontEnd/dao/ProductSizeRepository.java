package com.qtsneaker.ShopFrontEnd.dao;

import com.qtsneaker.common.entity.Province;
import com.qtsneaker.common.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSizeRepository extends JpaRepository<Size,Integer> {

}
