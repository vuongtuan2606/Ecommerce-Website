package com.qtsneaker.ShopFrontEnd.services.Impl;


import com.qtsneaker.ShopFrontEnd.dao.AdminSettingRepository;
import com.qtsneaker.ShopFrontEnd.setting.GeneralSettingBag;
import com.qtsneaker.ShopFrontEnd.services.AdminSettingService;
import com.qtsneaker.common.entity.setting.Setting;
import com.qtsneaker.common.entity.setting.SettingCategory;

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
