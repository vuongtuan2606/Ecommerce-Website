package com.tuanvuong.qtsnearker.controller.administrator;

import com.tuanvuong.qtsnearker.entity.Brand;
import com.tuanvuong.qtsnearker.entity.Category;
import com.tuanvuong.qtsnearker.entity.Product;
import com.tuanvuong.qtsnearker.security.AdminUserDetails;
import com.tuanvuong.qtsnearker.services.administrator.AdminBrandService;
import com.tuanvuong.qtsnearker.services.administrator.AdminCategoryService;
import com.tuanvuong.qtsnearker.services.administrator.AdminProductService;
import com.tuanvuong.qtsnearker.services.exceptions.ProductNotFoundException;
import com.tuanvuong.qtsnearker.util.FileUploadUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminProductController {


    @Autowired
    private AdminProductService adminProductService;

    @Autowired
    private AdminBrandService adminBrandService;

    @Autowired
    private AdminCategoryService adminCategoryService;

    @GetMapping("/products")
    public String listFirstPage(Model model) {
        return listByPage( "name", "asc", null,1, model,0);
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(@Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             @PathVariable(name = "pageNum") int pageNum,
                             Model model,
                             @Param("categoryId") Integer categoryId) {

        Page<Product> page = adminProductService.listByPage(pageNum, sortField, sortDir, keyword, categoryId);

        List<Product> listProduct = page.getContent();

        List<Category> listCategories = adminCategoryService.listCategoriesUsedInForm();

        long startCount = (pageNum - 1) * AdminProductService.PRODUCTS_PER_PAGE + 1;

        long endCount = startCount + AdminProductService.PRODUCTS_PER_PAGE - 1;

        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        if (categoryId != null) model.addAttribute("categoryId", categoryId);

        model.addAttribute("currentPage", pageNum);

        model.addAttribute("totalPages", page.getTotalPages());

        model.addAttribute("totalItem", page.getTotalElements());

        model.addAttribute("startCount", startCount);

        model.addAttribute("endCount", endCount);

        model.addAttribute("sortField", sortField);

        model.addAttribute("sortDir", sortDir);

        model.addAttribute("reverseSortDir", reverseSortDir);

        model.addAttribute("keyword", keyword);

        model.addAttribute("moduleURL", "/admin/products");

        model.addAttribute("listProduct", listProduct);

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Danh sách sản phẩm");

        return "administrator/product/product";
    }
    @GetMapping("/products/create")
    public String createProduct(Model model) {
        List<Brand> listBrand = adminBrandService.listAll();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("listBrand", listBrand);
        model.addAttribute("pageTitle", "Thêm mới");
        model.addAttribute("numberOfExistingExtraImages", 0);

        return "administrator/product/product_form";
    }


    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
                              @RequestParam(value = "extraImage",required = false) MultipartFile[] extraImageMultipart,
                              @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames,
                              @AuthenticationPrincipal AdminUserDetails loggedUser)
            throws IOException {

        // kiểm tra xem người dùng hiện tại có vai trò "Salesperson" không
        if(loggedUser.hasRole("Salesperson")){
            // nếu có thì saveProductPrice
            adminProductService.saveProductPrice(product);

            redirectAttributes.addFlashAttribute("message", "Sản phảm đã được lưu thành công");
            return "redirect:/admin/products";
        }

        AdminProductSaveHelper.setMainImageName(mainImageMultipart, product);
        AdminProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
        AdminProductSaveHelper.setNewExtraImageNames(extraImageMultipart, product);

        Product savedProduct = adminProductService.save(product);

        AdminProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultipart, savedProduct);

        // Xóa ExtraImages đã bị xóa trên form
        AdminProductSaveHelper.deleteExtraImagesWereRemovedOnForm(product);

        redirectAttributes.addFlashAttribute("message", "Sản phảm đã được lưu thành công");

        return "redirect:/admin/products";
    }
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            adminProductService.delete(id);

            String productExtraImagesDir = "../product-images/" + id + "/extras";
            String productImagesDir = "../product-images/" + id;

            FileUploadUtil.removeDir(productExtraImagesDir);
            FileUploadUtil.removeDir(productImagesDir);

            redirectAttributes.addFlashAttribute("message", "Sản phẩm có id: " + id + " đã được xóa thành công");

        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String updateProductEnabledStatus(@PathVariable("id") Integer id,
                                             @PathVariable("status") boolean enabled,
                                             RedirectAttributes redirectAttributes) {

        adminProductService.updateCategoryEnabledStatus(id, enabled);

        String status = enabled ? "enabled" : "disabled";

        String message = "The product ID " + id + " has been " + status;

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            Product product = adminProductService.get(id);
            List<Brand> listBrand = adminBrandService.listAll();

            // lấy số lượng ảnh hiện có
            Integer numberOfExistingExtraImages = product.getImages().size();

            model.addAttribute("product", product);
            model.addAttribute("listBrand", listBrand);
            model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm (ID: " + id + ")");
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

            return  "administrator/product/product_form";

        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/admin/products";
        }
    }
    @GetMapping("/products/detail/{id}")
    public String viewProductDetails(@PathVariable("id") Integer id,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            Product product = adminProductService.get(id);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Chi tiết sản phẩm:(ID: " + id + ")");

            return "administrator/product/product_detail_form";

        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/admin/products";
        }
    }

}
