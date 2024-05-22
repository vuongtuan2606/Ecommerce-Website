package com.qtsneaker.ShopBackEnd.controller;

import com.qtsneaker.ShopBackEnd.security.AdminUserDetails;
import com.qtsneaker.ShopBackEnd.services.Brand.AdminBrandService;
import com.qtsneaker.ShopBackEnd.services.Category.AdminCategoryService;
import com.qtsneaker.ShopBackEnd.services.Product.AdminProductService;
import com.qtsneaker.ShopBackEnd.services.Product.AdminSizeServices;
import com.qtsneaker.ShopBackEnd.util.FileUploadUtil;
import com.qtsneaker.common.entity.Brand;
import com.qtsneaker.common.entity.Category;
import com.qtsneaker.common.entity.Product;


import com.qtsneaker.common.entity.Size;
import com.qtsneaker.common.exception.ProductNotFoundException;
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
    @Autowired
    private AdminSizeServices adminSizeServices;
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

        return "/product/product";
    }
    @GetMapping("/products/create")
    public String createProduct(Model model) {
        List<Brand> listBrand = adminBrandService.listAll();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("listBrand", listBrand);

        List<Size> listSize =adminSizeServices.getListAll();
        model.addAttribute("listSize", listSize);

        model.addAttribute("pageTitle", "Thêm mới");
        model.addAttribute("numberOfExistingExtraImages", 0);

        return "/product/product_form";
    }


    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
                              @RequestParam(value = "extraImage",required = false) MultipartFile[] extraImageMultipart,
                              @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames,
                              @AuthenticationPrincipal AdminUserDetails loggedUser)
            throws IOException {


        // kiểm tra xem người dùng hiện tại có vai trò "Salesperson" khôn
        if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
            if (loggedUser.hasRole("Salesperson")) {
                // nếu có thì saveProductPrice
                adminProductService.saveProductPrice(product);
                redirectAttributes.addFlashAttribute("message", "Sản phảm đã được lưu thành công");
                return "redirect:/admin/products";
            }
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

        String message = "Sản phẩm có ID:" + id + " đã được " + status;

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal AdminUserDetails loggedUser) {
        try {
            Product product = adminProductService.get(id);
            List<Brand> listBrand = adminBrandService.listAll();
            List<Size> listSize =adminSizeServices.getListAll();
            // lấy số lượng ảnh hiện có
            Integer numberOfExistingExtraImages = product.getImages().size();

            boolean isReadOnlyForSalesperson = false;

            if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
                if (loggedUser.hasRole("Salesperson")) {
                    isReadOnlyForSalesperson = true;
                }
            }
            model.addAttribute("listSize", listSize);
            model.addAttribute("product", product);
            model.addAttribute("listBrand", listBrand);
            model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm (ID: " + id + ")");
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
            model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);

            return  "/product/product_form";

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

            return "/product/product_detail_form";

        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/admin/products";
        }
    }

}
