package com.tuanvuong.qtsnearker.controller.administrator;

import com.tuanvuong.qtsnearker.entity.Brand;
import com.tuanvuong.qtsnearker.entity.Category;
import com.tuanvuong.qtsnearker.entity.Product;
import com.tuanvuong.qtsnearker.services.BrandService;
import com.tuanvuong.qtsnearker.services.CategoryService;
import com.tuanvuong.qtsnearker.services.ProductService;
import com.tuanvuong.qtsnearker.services.exceptions.CategoryNotFoundException;
import com.tuanvuong.qtsnearker.services.exceptions.ProductNotFoundException;
import com.tuanvuong.qtsnearker.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/product")
    public String listAll(Model model){
        List<Product> listProduct = productService.ListProductAll();

        model.addAttribute("listProduct",listProduct);
        model.addAttribute("pageTitle","Danh sách sản phẩm");

        return "administrator/product/product";
    }

    @GetMapping("/product/create")
    public String createProduct(Model model){
        List<Brand> listBrand = brandService.listAll();
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("listBrand",listBrand);
        model.addAttribute("listCategories",listCategories);
        model.addAttribute("product",product);
        model.addAttribute("pageTitle","Thêm mới");

        return  "administrator/product/product_form";
    }

    @PostMapping("/product/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes,
                              @RequestParam("fileImage") MultipartFile mainImageMultipart,
                              @RequestParam("extraImage") MultipartFile[] extraImageMultipart)
            throws IOException {
        setMainImageName(mainImageMultipart, product);
        setExtraImageNames(extraImageMultipart, product);

        Product savedProduct = productService.save(product);

        saveUploadedImages(mainImageMultipart, extraImageMultipart, savedProduct);

        redirectAttributes.addFlashAttribute("message", "Sản phảm đã được lưu thành công");

        return "redirect:/product";
    }

    private void saveUploadedImages(MultipartFile mainImageMultipart,
                                    MultipartFile[] extraImageMultipart, Product savedProduct) throws IOException {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            String uploadDir = "src/main/resources/static/product-images/" + savedProduct.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
        }

        if (extraImageMultipart.length > 0) {
            String uploadDir = "src/main/resources/static/product-images/" + savedProduct.getId() + "/extras";

            for (MultipartFile multipartFile : extraImageMultipart) {
                if (multipartFile.isEmpty()) continue;

                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            }
        }
    }

    private void setExtraImageNames(MultipartFile[] extraImageMultipart, Product product) {
        if (extraImageMultipart.length > 0) {
            for (MultipartFile multipartFile : extraImageMultipart) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    product.addExtraImages(fileName);
                }
            }
        }
    }

    private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }


    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            productService.delete(id);
            String productExtraImagesDir = "src/main/resources/static/product-images/" + id + "/extras";
            String productImagesDir = "src/main/resources/static/product-images/" + id;

            FileUploadUtil.removeDir(productExtraImagesDir);
            FileUploadUtil.removeDir(productImagesDir);

            redirectAttributes.addFlashAttribute("message", "Sản phẩm có id: " + id + " đã được xóa thành công");
        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/product";
    }

    @GetMapping("/product/{id}/enabled/{status}")
    public String updateProductEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled,
                                              RedirectAttributes redirectAttributes) {

        productService.updateCategoryEnabledStatus(id, enabled);

        String status = enabled ? "enabled" : "disabled";

        String message = "The product ID " + id + " has been " + status;

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin/product";
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.get(id);
            List<Brand> listBrand = brandService.listAll();

            // lấy số lượng ảnh hiện có
            Integer numberOfExistingExtraImages = product.getImages().size();

            model.addAttribute("product", product);
            model.addAttribute("listBrand", listBrand);
            model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm (ID: " + id + ")");
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

            return  "administrator/product/product_form";

        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/product";
        }
    }
}
