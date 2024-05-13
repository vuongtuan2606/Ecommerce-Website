package com.qtsneaker.ShopBackEnd.controller;


import com.qtsneaker.ShopBackEnd.dao.AdminCurrencyRepository;
import com.qtsneaker.ShopBackEnd.services.Setting.AdminSettingService;
import com.qtsneaker.ShopBackEnd.setting.GeneralSettingBag;
import com.qtsneaker.ShopBackEnd.util.FileUploadUtil;
import com.qtsneaker.common.entity.Currency;
import com.qtsneaker.common.entity.setting.Setting;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminSettingControler {

    @Autowired
    private AdminSettingService settingService;

    @Autowired
    private AdminCurrencyRepository adminCurrencyRepository;

    @GetMapping("/setting")
    public String findAll(Model model){

        List<Setting> listSettings = settingService.listAllSettings();

        List<Currency> listCurrencies = adminCurrencyRepository.findAllByOrderByNameAsc();

        model.addAttribute("listCurrencies", listCurrencies);
        model.addAttribute("pageTitle", "setting");

        for (Setting setting : listSettings) {
            //thêm các cặp key-value từ danh sách listSettings vào model.
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        return "/setting/setting";
    }
    @PostMapping("/setting/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes)
            throws IOException {

        GeneralSettingBag settingBag = settingService.getGeneralSettings();

        saveSiteLogo(multipartFile, settingBag);

        saveCurrencySymbol(request, settingBag);

        updateSettingValuesFromForm(request, settingBag.list());

        redirectAttributes.addFlashAttribute("message", "Cài đặt đã được lưu thành công.");

        return "redirect:/admin/setting";
    }

    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
        if (!multipartFile.isEmpty()) {

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            String value = "/site-logo/" + fileName;

            settingBag.updateSiteLogo(value);

            String uploadDir = "../site-logo/";

            FileUploadUtil.cleanDir(uploadDir);

            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {

        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));

        Optional<Currency> findByIdResult = adminCurrencyRepository.findById(currencyId);

        if (findByIdResult.isPresent()) {
            Currency currency = findByIdResult.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }

    private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {

        for (Setting setting : listSettings) {

            String value = request.getParameter(setting.getKey());

            if (value != null) {
                setting.setValue(value);
            }
        }

        settingService.saveAll(listSettings);
    }

    @PostMapping("/setting/save_mail_server")
    public String saveMailServerSetttings(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        List<Setting> mailServerSettings = settingService.getMailServerSettings();

        updateSettingValuesFromForm(request, mailServerSettings);

        redirectAttributes.addFlashAttribute("message", "Cài đặt máy chủ mail đã được lưu");

        return "redirect:/admin/setting";
    }
    @PostMapping("/setting/save_mail_templates")
    public String saveMailTemplateSetttings(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        List<Setting> mailTemplateSettings = settingService.getMailTemplateSettings();

        updateSettingValuesFromForm(request, mailTemplateSettings);

        redirectAttributes.addFlashAttribute("message", "Cài đặt mẫu gửi mail đã được lưu");

        return "redirect:/admin/setting";
    }

    @PostMapping("/settings/save_payment")
    public String savePaymentSetttings(HttpServletRequest request, RedirectAttributes ra) {
        List<Setting> paymentSettings = settingService.getPaymentSettings();
        updateSettingValuesFromForm(request, paymentSettings);

        ra.addFlashAttribute("message", "Cài đặt thanh toán đã được lưu");

        return "redirect:/admin/setting";
    }


}
