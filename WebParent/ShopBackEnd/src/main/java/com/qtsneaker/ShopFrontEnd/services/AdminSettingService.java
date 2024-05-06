package com.qtsneaker.ShopFrontEnd.services;

import com.qtsneaker.ShopFrontEnd.setting.GeneralSettingBag;
import com.qtsneaker.common.entity.setting.Setting;


import java.util.List;

public interface AdminSettingService {
    List<Setting> listAllSettings();

    public GeneralSettingBag getGeneralSettings();

    void saveAll(Iterable<Setting> settings);
    List<Setting> getMailServerSettings();

    List<Setting> getMailTemplateSettings();
}
