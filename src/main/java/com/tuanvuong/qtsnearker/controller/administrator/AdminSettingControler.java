package com.tuanvuong.qtsnearker.controller.administrator;

import com.tuanvuong.qtsnearker.dao.administrator.AdminCurrencyRepository;
import com.tuanvuong.qtsnearker.entity.Currency;
import com.tuanvuong.qtsnearker.entity.setting.Setting;
import com.tuanvuong.qtsnearker.entity.setting.administrator.GeneralSettingBag;
import com.tuanvuong.qtsnearker.services.administrator.AdminSettingService;
import com.tuanvuong.qtsnearker.util.FileUploadUtil;
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
    private AdminCurrencyRepository currencyRepository;

    @GetMapping("/setting")
    public String findAll(Model model){
        List<Setting> listSettings = settingService.listAllSettings();

        List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();

        model.addAttribute("listCurrencies", listCurrencies);
        model.addAttribute("pageTitle", "setting");

        for (Setting setting : listSettings) {
            //thêm các cặp key-value từ danh sách listSettings vào model.
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        return "administrator/setting/setting";
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

        redirectAttributes.addFlashAttribute("message", "General settings have been saved.");

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

        Optional<Currency> findByIdResult = currencyRepository.findById(currencyId);

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


}
