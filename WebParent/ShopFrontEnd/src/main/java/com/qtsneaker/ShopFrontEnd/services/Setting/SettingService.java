package com.qtsneaker.ShopFrontEnd.services.Setting;



import com.qtsneaker.ShopFrontEnd.setting.CurrencySettingBag;
import com.qtsneaker.ShopFrontEnd.setting.EmailSettingBag;
import com.qtsneaker.common.entity.setting.Setting;

import java.util.List;

public interface SettingService {

    public List<Setting> getGeneralSettings();
    public EmailSettingBag getEmailSettings();

    CurrencySettingBag getCurrencySettings();
}
