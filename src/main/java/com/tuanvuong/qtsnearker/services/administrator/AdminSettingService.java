package com.tuanvuong.qtsnearker.services.administrator;

import com.tuanvuong.qtsnearker.entity.setting.Setting;
import com.tuanvuong.qtsnearker.entity.setting.administrator.GeneralSettingBag;

import java.util.List;

public interface AdminSettingService {
    List<Setting> listAllSettings();

    public GeneralSettingBag getGeneralSettings();

    void saveAll(Iterable<Setting> settings);
    List<Setting> getMailServerSettings();

    List<Setting> getMailTemplateSettings();
}
