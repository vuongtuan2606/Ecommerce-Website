package com.tuanvuong.qtsnearker.controller.administrator;
import com.tuanvuong.qtsnearker.entity.User;
import com.tuanvuong.qtsnearker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
    @Autowired
    private UserService userService;
    @GetMapping("/home")
    public String HomeAdmin(){
        return "administrator/home";
    }

}
