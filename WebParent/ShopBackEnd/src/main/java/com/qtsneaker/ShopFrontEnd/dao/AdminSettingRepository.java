package com.qtsneaker.ShopFrontEnd.dao;


import com.qtsneaker.common.entity.setting.Setting;
import com.qtsneaker.common.entity.setting.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AdminSettingRepository extends JpaRepository<Setting, String> {
    public List<Setting> findByCategory(SettingCategory category);
}
