package com.qtsneaker.ShopBackEnd.services.Setting;

import com.qtsneaker.ShopBackEnd.setting.GeneralSettingBag;
import com.qtsneaker.common.entity.setting.Setting;


import java.util.List;

public interface AdminSettingService {
    List<Setting> listAllSettings();

    public GeneralSettingBag getGeneralSettings();

    void saveAll(Iterable<Setting> settings);
    List<Setting> getMailServerSettings();

    List<Setting> getMailTemplateSettings();

    public List<Setting> getCurrencySettings();

    public List<Setting> getPaymentSettings();
}
