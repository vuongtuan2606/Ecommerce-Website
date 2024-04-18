package com.tuanvuong.qtsnearker.controller.administrator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/showMyLoginPage")
    public String viewLoginPage(){
        return "administrator/pages-login";
    }
}
