package com.tuanvuong.qtsnearker.controller.administrator;

import com.tuanvuong.qtsnearker.dto.CategoryPageInfo;
import com.tuanvuong.qtsnearker.entity.Category;
import com.tuanvuong.qtsnearker.services.CategoryService;
import com.tuanvuong.qtsnearker.services.exceptions.CategoryNotFoundException;
import com.tuanvuong.qtsnearker.util.CategoryExcelExporter;
import com.tuanvuong.qtsnearker.util.FileUploadUtil;
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
public class AdminCategoryController {
    @Autowired
    private  CategoryService categoryService;

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

        List<Category> listCategories = categoryService.listByPage(pageInfo, pageNum, sortDir,keyword);

        long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;

        long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;

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

        model.addAttribute("moduleURL", "/categories");

        model.addAttribute("pageTitle","Danh sách danh mục");

        return "administrator/category/categories";
    }
    @GetMapping("/categories/create")
    public String newCategories(Model model){

        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", listCategories);

        model.addAttribute("pageTitle","Thêm mới");

        return "administrator/category/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategories(Category category,
                               RedirectAttributes redirectAttributes,
                               @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            category.setImage(fileName);

            Category savedCategory = categoryService.save(category);

            // đường dẫn thư mục ->  tạo thư mục "category-images"
            String uploadDir = "src/main/resources/static/category-images/" +savedCategory.getId();

            // xóa ảnh cũ
            FileUploadUtil.cleanDir(uploadDir);

            // lưu tệp vào mục chỉ định
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else {
            categoryService.save(category);
        }

        redirectAttributes.addFlashAttribute("message","The categories has been saved successfully");

        return "redirect:/categories";

    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id, Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.get(id);

            List<Category> categoryList = categoryService.listCategoriesUsedInForm();

            model.addAttribute("category", category);

            model.addAttribute("categoryList", categoryList);

            model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");

            return "administrator/category/category_form";

        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled,
                                              RedirectAttributes redirectAttributes) {

        categoryService.updateCategoryEnabledStatus(id, enabled);

        String status = enabled ? "enabled" : "disabled";

        String message = "The category ID " + id + " has been " + status;

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("message", "The category ID " + id + " has been deleted successfully");

        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/categories";
    }

    /*
     * xuất file excel
     * */
    @GetMapping("/categories/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException{

        List<Category> listCate = categoryService.listCategoriesUsedInForm();

        CategoryExcelExporter exporter = new CategoryExcelExporter();

        exporter.export(listCate, response);

    }

}
