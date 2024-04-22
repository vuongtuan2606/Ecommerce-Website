package com.tuanvuong.qtsnearker.controller.administrator;

import com.tuanvuong.qtsnearker.entity.Brand;
import com.tuanvuong.qtsnearker.entity.Category;
import com.tuanvuong.qtsnearker.entity.Product;
import com.tuanvuong.qtsnearker.services.BrandService;
import com.tuanvuong.qtsnearker.services.CategoryService;
import com.tuanvuong.qtsnearker.services.ProductService;
import com.tuanvuong.qtsnearker.services.exceptions.CategoryNotFoundException;
import com.tuanvuong.qtsnearker.services.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String saveProduct(Product product,
                               RedirectAttributes redirectAttributes){

        productService.save(product);
        redirectAttributes.addFlashAttribute("message","Sản phẩm đã được lưu thành công");

        return "redirect:/admin/product";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            productService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Sản phẩm có id: " + id + " đã được xóa thành công");

        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/admin/product";
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
}
