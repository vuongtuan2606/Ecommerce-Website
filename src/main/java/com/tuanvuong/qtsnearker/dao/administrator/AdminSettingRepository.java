package com.tuanvuong.qtsnearker.dao.administrator;


import com.tuanvuong.qtsnearker.entity.setting.Setting;
import com.tuanvuong.qtsnearker.entity.setting.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AdminSettingRepository extends JpaRepository<Setting, String> {
    public List<Setting> findByCategory(SettingCategory category);
}
