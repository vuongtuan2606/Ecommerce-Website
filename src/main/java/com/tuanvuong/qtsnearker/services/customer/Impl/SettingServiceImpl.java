package com.tuanvuong.qtsnearker.services.customer.Impl;

import com.tuanvuong.qtsnearker.dao.customer.SettingRepository;
import com.tuanvuong.qtsnearker.entity.setting.Setting;
import com.tuanvuong.qtsnearker.entity.setting.SettingCategory;
import com.tuanvuong.qtsnearker.services.customer.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;


    @Override
    public List<Setting> getGeneralSettings() {
        return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }


}
