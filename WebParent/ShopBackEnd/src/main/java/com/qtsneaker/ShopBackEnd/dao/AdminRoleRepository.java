package com.qtsneaker.ShopBackEnd.dao;

import com.qtsneaker.common.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminRoleRepository extends JpaRepository<Role,Integer> {


}
