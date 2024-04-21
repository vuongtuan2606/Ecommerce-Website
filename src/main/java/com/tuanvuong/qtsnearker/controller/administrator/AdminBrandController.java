package com.tuanvuong.qtsnearker.controller.administrator;

import com.tuanvuong.qtsnearker.entity.Brand;
import com.tuanvuong.qtsnearker.entity.Category;
import com.tuanvuong.qtsnearker.services.BrandService;
import com.tuanvuong.qtsnearker.services.CategoryService;
import com.tuanvuong.qtsnearker.services.exceptions.BrandNotFoundException;
import com.tuanvuong.qtsnearker.util.BrandExcelExporter;
import com.tuanvuong.qtsnearker.util.FileUploadUtil;
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
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brand")
    public String listFirstPage(Model model) {
        return listByPage("asc", null,1 ,model);
    }

    @GetMapping("/brand/page/{pageNum}")
    public String listByPage(@Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             @PathVariable(name = "pageNum") int pageNum,
                             Model model){

        Page<Brand> page = brandService.listByPage(pageNum,sortDir,keyword);

        List<Brand> listBrand = page.getContent();

        long startCount = (pageNum - 1) * BrandService.BRAND_PER_PAGE + 1;

        long endCount = startCount + BrandService.BRAND_PER_PAGE - 1;

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

        model.addAttribute("moduleURL", "/admin/brand");

        model.addAttribute("pageTitle","Danh sách thương hiệu");

        return "administrator/brand/brand";
    }

    @GetMapping("/brand/create")
    public  String newBrands(Model model){
        Brand brand = new Brand();
        List<Category> listCategory = categoryService.listCategoriesUsedInForm();

        model.addAttribute("listCategory", listCategory);
        model.addAttribute("brand", brand);
        model.addAttribute("pageTitle","Thêm mới");
        return "administrator/brand/brand_form";

    }

    @PostMapping("/brand/save")
    public String saveBrands(Brand brand,
                             RedirectAttributes redirectAttributes,
                             @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            brand.setLogo(fileName);

            Brand savedBrand = brandService.save(brand);

            // đường dẫn thư mục ->  tạo thư mục "category-images"
            String uploadDir = "src/main/resources/static/brand-logo/" +savedBrand.getId();

            // xóa ảnh cũ
            FileUploadUtil.cleanDir(uploadDir);

            // lưu tệp vào mục chỉ định
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else {
            brandService.save(brand);
        }

        redirectAttributes.addFlashAttribute("message","The brand has been saved successfully");

        return "redirect:/admin/brand";

    }
    @GetMapping("/brand/edit/{id}")
    public String editBrands(@PathVariable(name = "id") Integer id, Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Brand brand = brandService.get(id);

            List<Category> listCategory = categoryService.listCategoriesUsedInForm();

            model.addAttribute("brand", brand);

            model.addAttribute("listCategory", listCategory);

            model.addAttribute("pageTitle", "Edit brand (ID: " + id + ")");

            return "administrator/brand/brand_form";

        } catch (BrandNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/admin/brand";
        }
    }
    @GetMapping("/brand/delete/{id}")
    public String deleteBrands(@PathVariable(name = "id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            brandService.delete(id);
            redirectAttributes.addFlashAttribute("message", "The brand ID " + id + " has been deleted successfully");

        } catch (BrandNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/admin/brand";
    }

    @GetMapping("/brand/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException{

        List<Brand> listBrand = brandService.listAll();

        BrandExcelExporter exporter = new BrandExcelExporter();

        exporter.export(listBrand, response);

    }


}
