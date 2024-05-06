package com.qtsneaker.ShopFrontEnd.services;



import com.qtsneaker.ShopFrontEnd.setting.EmailSettingBag;
import com.qtsneaker.common.entity.setting.Setting;

import java.util.List;

public interface SettingService {

    public List<Setting> getGeneralSettings();
    public EmailSettingBag getEmailSettings();
}
