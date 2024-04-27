package com.tuanvuong.qtsnearker.dao.administrator;

import com.tuanvuong.qtsnearker.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminRoleRepository extends JpaRepository<Role,Integer> {


}
