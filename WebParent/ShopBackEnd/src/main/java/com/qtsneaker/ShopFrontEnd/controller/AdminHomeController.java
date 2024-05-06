package com.qtsneaker.ShopFrontEnd.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {
    @GetMapping("/")
    public String HomeAdmin(){
        return "/index";
    }

}
