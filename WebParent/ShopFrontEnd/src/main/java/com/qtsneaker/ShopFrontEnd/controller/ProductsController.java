package com.qtsneaker.ShopFrontEnd.controller;


import com.qtsneaker.ShopFrontEnd.services.CartService;
import com.qtsneaker.ShopFrontEnd.services.CategoryService;
import com.qtsneaker.ShopFrontEnd.services.ProductService;
import com.qtsneaker.common.entity.Cart;
import com.qtsneaker.common.entity.Category;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.Product;
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

import java.util.List;

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
    @GetMapping("/categories/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
                                        Model model) {

        return viewCategoryByPage(alias, 1, model);
    }
    @GetMapping("/categories/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias,
                                     @PathVariable("pageNum") int pageNum,
                                     Model model) {
        try {

            // tìm Category theo alias
            Category category = categoryService.getCategory(alias);

            // list Category cha không có Category con
            List<Category> listNoChildrenCategories = categoryService.listNoChildrenCategories();

            // list Category cha  có Category con
            List<Category> listCategoryParents = categoryService.getCategoryParents(category);


            Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());

            List<Product> listProducts = pageProducts.getContent();

            long startCount = (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
            long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;
            if (endCount > pageProducts.getTotalElements()) {
                endCount = pageProducts.getTotalElements();
            }


            model.addAttribute("listNoChildrenCategories",listNoChildrenCategories);
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("totalPages", pageProducts.getTotalPages());
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
            model.addAttribute("totalItems", pageProducts.getTotalElements());
            model.addAttribute("listProducts", listProducts);

            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("pageTitle", category.getName());


            model.addAttribute("category", category);

            return "product/shop-by-category";
        } catch (CategoryNotFoundException ex) {
            return "error/404";
        }
    }

    @GetMapping("/shop")
    public String viewAllFirstPage(Model model) {

        return viewAllProduct(1,model);
    }
    @GetMapping("/shop/page/{pageNum}")
    public String viewAllProduct( @PathVariable("pageNum") int pageNum,
                                     Model model) {

            Page<Product> pageProducts = productService.listAllProduct(pageNum);
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

            return "product/shop-all-product";
    }


    @GetMapping("/product/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias,
                                    Model model , HttpServletRequest request){

        try {
            Customer customer = controllerHelper.getAuthenticatedCustomer(request);
            List<Cart> cartItems = cartService.listCartItems(customer);

            Product product = productService.getProduct(alias);
            List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());

            Integer categoryId = product.getCategory().getId();
            List<Product> similarProducts = productService.findTop4SimilarProducts(categoryId, alias);

            model.addAttribute("similarProducts", similarProducts);
            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("product", product);
            model.addAttribute("cartItems",cartItems);

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
