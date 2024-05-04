package com.qtsneaker.ShopBackEnd.controller;

import com.qtsneaker.ShopBackEnd.exception.BrandNotFoundException;
import com.qtsneaker.ShopBackEnd.services.AdminBrandService;
import com.qtsneaker.ShopBackEnd.services.AdminCategoryService;
import com.qtsneaker.ShopBackEnd.util.BrandExcelExporter;
import com.qtsneaker.ShopBackEnd.util.FileUploadUtil;
import com.qtsneaker.common.entity.Brand;
import com.qtsneaker.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminBrandController {
    @Autowired
    private AdminBrandService adminBrandService;

    @Autowired
    private AdminCategoryService adminCategoryService;

    @GetMapping("/brands")
    public String listFirstPage(Model model) {
        return listByPage("asc", null,1 ,model);
    }

    @GetMapping("/brands/page/{pageNum}")
    public String listByPage(@Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             @PathVariable(name = "pageNum") int pageNum,
                             Model model){

        Page<Brand> page = adminBrandService.listByPage(pageNum,sortDir,keyword);

        List<Brand> listBrand = page.getContent();

        long startCount = (pageNum - 1) * AdminBrandService.BRAND_PER_PAGE + 1;

        long endCount = startCount + AdminBrandService.BRAND_PER_PAGE - 1;

        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItem", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);

        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);

        model.addAttribute("listBrand", listBrand);
        model.addAttribute("reverseSortDir", reverseSortDir);

        model.addAttribute("moduleURL", "/admin/brands");

        model.addAttribute("pageTitle","Danh sách thương hiệu");

        return "/brand/brand";
    }

    @GetMapping("/brands/create")
    public  String newBrands(Model model){
        Brand brand = new Brand();
        List<Category> listCategory = adminCategoryService.listCategoriesUsedInForm();

        model.addAttribute("listCategory", listCategory);
        model.addAttribute("brand", brand);
        model.addAttribute("pageTitle","Thêm mới");
        return "/brand/brand_form";

    }

    @PostMapping("/brands/save")
    public String saveBrands(Brand brand,
                             RedirectAttributes redirectAttributes,
                             @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            brand.setLogo(fileName);

            Brand savedBrand = adminBrandService.save(brand);

            // đường dẫn thư mục ->  tạo thư mục "category-images"
            String uploadDir = "../brand-logo/" +savedBrand.getId();

            // xóa ảnh cũ
            FileUploadUtil.cleanDir(uploadDir);

            // lưu tệp vào mục chỉ định
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else {
            adminBrandService.save(brand);
        }

        redirectAttributes.addFlashAttribute("message","The brand has been saved successfully");

        return "redirect:/admin/brands";

    }
    @GetMapping("/brands/edit/{id}")
    public String editBrands(@PathVariable(name = "id") Integer id, Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Brand brand = adminBrandService.get(id);

            List<Category> listCategory = adminCategoryService.listCategoriesUsedInForm();

            model.addAttribute("brand", brand);

            model.addAttribute("listCategory", listCategory);

            model.addAttribute("pageTitle", "Edit brand (ID: " + id + ")");

            return "/brand/brand_form";

        } catch (BrandNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/admin/brands";
        }
    }
    @GetMapping("/brands/delete/{id}")
    public String deleteBrands(@PathVariable(name = "id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            adminBrandService.delete(id);
            redirectAttributes.addFlashAttribute("message", "The brand ID " + id + " has been deleted successfully");

        } catch (BrandNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/admin/brands";
    }

    @GetMapping("/brands/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException{

        List<Brand> listBrand = adminBrandService.listAll();

        BrandExcelExporter exporter = new BrandExcelExporter();

        exporter.export(listBrand, response);

    }


}
