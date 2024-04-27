package com.tuanvuong.qtsnearker.controller.customer;

import com.tuanvuong.qtsnearker.entity.Brand;
import com.tuanvuong.qtsnearker.entity.Category;
import com.tuanvuong.qtsnearker.services.customer.BrandsService;
import com.tuanvuong.qtsnearker.services.customer.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/index")
    public String Home(Model model) {

        List<Category> listNoChildrenCategories = categoryService.listNoChildrenCategories();

        model.addAttribute("listNoChildrenCategories",listNoChildrenCategories);

        return "customer/homePage";
    }


}
