package com.qtsneaker.ShopBackEnd.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminHomeController {
    @GetMapping("/")
    public String HomeAdmin(){
        return "/index";
    }

}
