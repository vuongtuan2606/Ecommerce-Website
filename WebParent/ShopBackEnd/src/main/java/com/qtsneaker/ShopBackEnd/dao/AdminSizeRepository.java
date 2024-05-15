package com.qtsneaker.ShopBackEnd.dao;

import com.qtsneaker.common.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminSizeRepository extends JpaRepository<Size,Integer> {
}
