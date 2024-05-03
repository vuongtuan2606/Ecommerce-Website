package com.tuanvuong.qtsnearker.services.customer;

import com.tuanvuong.qtsnearker.entity.setting.Setting;
import com.tuanvuong.qtsnearker.entity.setting.customer.EmailSettingBag;

import java.util.List;

public interface SettingService {

    public List<Setting> getGeneralSettings();
    public EmailSettingBag getEmailSettings();
}
