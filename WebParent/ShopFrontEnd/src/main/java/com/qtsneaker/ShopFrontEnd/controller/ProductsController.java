package com.qtsneaker.ShopFrontEnd.controller;


import com.qtsneaker.ShopFrontEnd.dao.ProductRepository;
import com.qtsneaker.ShopFrontEnd.services.Cart.CartService;
import com.qtsneaker.ShopFrontEnd.services.Category.CategoryService;
import com.qtsneaker.ShopFrontEnd.services.Product.ProductService;
import com.qtsneaker.common.entity.*;
import com.qtsneaker.common.exception.CategoryNotFoundException;
import com.qtsneaker.common.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class ProductsController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ControllerHelper controllerHelper;

    @Autowired
    private CartService cartService;

    @Autowired private ProductRepository repository;
    @GetMapping("/categories/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
                                        Model model) {
        return viewCategoryByPage(alias, "price", "desc", 1, model);
    }

    @GetMapping("/categories/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias,
                                     @RequestParam(value = "sortField", defaultValue = "price") String sortField,
                                     @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                     @PathVariable("pageNum") int pageNum,
                                     Model model) {
        try {
            // tìm Category theo alias
            Category category = categoryService.getCategory(alias);

            // list Category cha không có Category con
            List<Category> listNoChildrenCategories = categoryService.listNoChildrenCategories();

            // list Category cha có Category con
            List<Category> listCategoryParents = categoryService.getCategoryParents(category);

            // list products theo phân trang và sắp xếp
            Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId(), sortField, sortDir);
            List<Product> listProducts = pageProducts.getContent();

            long startCount = (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
            long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;
            if (endCount > pageProducts.getTotalElements()) {
                endCount = pageProducts.getTotalElements();
            }

            model.addAttribute("listNoChildrenCategories", listNoChildrenCategories);
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("totalPages", pageProducts.getTotalPages());
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
            model.addAttribute("totalItems", pageProducts.getTotalElements());
            model.addAttribute("listProducts", listProducts);
            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("pageTitle", category.getName());
            model.addAttribute("category", category);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

            return "product/shop-by-category";
        } catch (CategoryNotFoundException ex) {
            return "error/404";
        }
    }

    @GetMapping("/shop")
    public String viewAllFirstPage(Model model) {

        return viewAllProduct(1,"price","asc", model);
    }
    @GetMapping("/shop/page/{pageNum}")
    public String viewAllProduct( @PathVariable("pageNum") int pageNum,
                                  @Param("sortField") String sortField,
                                  @Param("sortDir") String sortDir,
                                  Model model) {

        Page<Product> pageProducts = productService.listAllProduct(pageNum, sortField, sortDir);
        List<Product> listProducts = pageProducts.getContent();

        long startCount = (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;
        if (endCount > pageProducts.getTotalElements()) {
            endCount = pageProducts.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("pageTitle","Shop");
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "product/shop-all-product";
    }

    @GetMapping("/sale")
    public String viewAllProductSaleOf(Model model) {

        return viewAllProductSaleOf(1,"price","asc", model);
    }
    @GetMapping("/sale/page/{pageNum}")
    public String viewAllProductSaleOf( @PathVariable("pageNum") int pageNum,
                                        @Param("sortField") String sortField,
                                        @Param("sortDir") String sortDir,
                                        Model model) {

        Page<Product> pageProducts = productService.listProductSaleOf(pageNum,sortField, sortDir);
        List<Product> listProducts = pageProducts.getContent();

        long startCount = (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;
        if (endCount > pageProducts.getTotalElements()) {
            endCount = pageProducts.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("pageTitle","Sản phẩm giảm giá");
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "product/shop-sale-of";
    }


    @GetMapping("/product/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias,
                                    Model model , HttpServletRequest request){

        try {
            Customer customer = controllerHelper.getAuthenticatedCustomer(request);
            List<Cart> cartItems = cartService.listCartItems(customer);

            Product product = productService.getProduct(alias);
            List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());

            Set<Size> sortedSizes = new TreeSet<>(Comparator.comparing(Size::getName));
            sortedSizes.addAll(product.getSizes());

            Integer categoryId = product.getCategory().getId();
            List<Product> similarProducts = productService.findTop4SimilarProducts(categoryId, alias);

            model.addAttribute("similarProducts", similarProducts);
            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("product", product);
            model.addAttribute("cartItems",cartItems);
            model.addAttribute("sortedSizes", sortedSizes);
            model.addAttribute("pageTitle", product.getShortName());

            return "product/product-details";
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String searchFirstPage(@Param("keyword") String keyword,
                                  Model model) {
        return searchByPage(keyword, 1, model);
    }

    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(@Param("keyword") String keyword,
                               @PathVariable("pageNum") int pageNum,
                               Model model) {

        Page<Product> pageProducts = productService.search(keyword, pageNum);
        List<Product> listResult = pageProducts.getContent();

        long startCount = (pageNum - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;
        if (endCount > pageProducts.getTotalElements()) {
            endCount = pageProducts.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("pageTitle", keyword + " - Search Result");
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchKeyword", keyword);
        model.addAttribute("listResult", listResult);

        return "product/search-result";
    }
}
