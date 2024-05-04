package com.qtsneaker.ShopFrontEnd.controller;


import com.qtsneaker.ShopFrontEnd.services.CategoryService;
import com.qtsneaker.common.entity.Category;
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

        return "/homePage";
    }


}
