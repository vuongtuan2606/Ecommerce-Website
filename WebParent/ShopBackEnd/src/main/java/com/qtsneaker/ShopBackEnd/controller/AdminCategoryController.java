package com.qtsneaker.ShopBackEnd.controller;


import com.qtsneaker.ShopBackEnd.dto.CategoryPageInfo;
import com.qtsneaker.ShopBackEnd.services.AdminCategoryService;
import com.qtsneaker.ShopBackEnd.util.CategoryExcelExporter;
import com.qtsneaker.ShopBackEnd.util.FileUploadUtil;
import com.qtsneaker.common.entity.Category;

import com.qtsneaker.common.exception.CategoryNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminCategoryController {
    @Autowired
    private AdminCategoryService adminCategoryService;

    @GetMapping("/categories")
    public String listFirstPage(Model model) {
        return listByPage("asc", null, 1,model);
    }
    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             @PathVariable(name = "pageNum") int pageNum,
                             Model model){

        CategoryPageInfo pageInfo = new CategoryPageInfo();

        List<Category> listCategories = adminCategoryService.listByPage(pageInfo, pageNum, sortDir,keyword);

        long startCount = (pageNum - 1) * AdminCategoryService.ROOT_CATEGORIES_PER_PAGE + 1;

        long endCount = startCount + AdminCategoryService.ROOT_CATEGORIES_PER_PAGE - 1;

        if (endCount > pageInfo.getTotalElements()) {
            endCount = pageInfo.getTotalElements();
        }

        // đảo ngược giá trị
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("totalItem", pageInfo.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);

        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);

        model.addAttribute("listCategory", listCategories);
        model.addAttribute("reverseSortDir", reverseSortDir);

        model.addAttribute("moduleURL", "/admin/categories");

        model.addAttribute("pageTitle","Danh sách danh mục");

        return "/category/categories";
    }
    @GetMapping("/categories/create")
    public String newCategories(Model model){

        List<Category> listCategories = adminCategoryService.listCategoriesUsedInForm();

        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", listCategories);

        model.addAttribute("pageTitle","Thêm mới");

        return "/category/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategories(Category category,
                               RedirectAttributes redirectAttributes,
                               @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            category.setImage(fileName);

            Category savedCategory = adminCategoryService.save(category);

            // đường dẫn thư mục ->  tạo thư mục "category-images"
            String uploadDir = "../category-images/" +savedCategory.getId();

            // xóa ảnh cũ
            FileUploadUtil.cleanDir(uploadDir);

            // lưu tệp vào mục chỉ định
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else {
            adminCategoryService.save(category);
        }

        redirectAttributes.addFlashAttribute("message","Danh mục đã được lưu thành công!");

        return "redirect:/admin/categories";

    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id, Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Category category = adminCategoryService.get(id);

            List<Category> categoryList = adminCategoryService.listCategoriesUsedInForm();

            model.addAttribute("category", category);

            model.addAttribute("categoryList", categoryList);

            model.addAttribute("pageTitle", "Chỉnh sửa dnah mục (ID: " + id + ")");

            return "/category/category_form";

        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/admin/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled,
                                              RedirectAttributes redirectAttributes) {

        adminCategoryService.updateCategoryEnabledStatus(id, enabled);

        String status = enabled ? "enabled" : "disabled";

        String message = "Danh mục c ID: " + id + " đã được " + status;

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            adminCategoryService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Danh mục có ID: " + id + " đã được xóa thành công");

        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/admin/categories";
    }

    /*
     * xuất file excel
     * */
    @GetMapping("/categories/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException{

        List<Category> listCate = adminCategoryService.listCategoriesUsedInForm();

        CategoryExcelExporter exporter = new CategoryExcelExporter();

        exporter.export(listCate, response);

    }

}
