package com.tuanvuong.qtsnearker.services.administrator.Impl;

import com.tuanvuong.qtsnearker.dao.administrator.AdminSettingRepository;
import com.tuanvuong.qtsnearker.entity.setting.Setting;
import com.tuanvuong.qtsnearker.entity.setting.SettingCategory;
import com.tuanvuong.qtsnearker.entity.setting.administrator.GeneralSettingBag;
import com.tuanvuong.qtsnearker.services.administrator.AdminSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminSettingServiceImpl implements AdminSettingService {

    @Autowired
    private AdminSettingRepository repository;

    @Override
    public List<Setting> listAllSettings() {
        return repository.findAll();
    }

    @Override
    public GeneralSettingBag getGeneralSettings() {
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = repository.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = repository.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);

        return new GeneralSettingBag(settings);
    }

    @Override
    public void saveAll(Iterable<Setting> settings) {
            repository.saveAll(settings);
    }

    @Override
    public List<Setting> getMailServerSettings() {
        return repository.findByCategory(SettingCategory.MAIL_SERVER);
    }

    @Override
    public List<Setting> getMailTemplateSettings() {
        return repository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

}
