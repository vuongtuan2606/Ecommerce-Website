package com.qtsneaker.ShopBackEnd.rest;


import com.qtsneaker.ShopBackEnd.services.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminUserRestController {
    @Autowired
    private AdminUserService adminUserService;
    @PostMapping("/users/check_email")
    public  String checkDuplicateEmail(@Param("id") Integer id,
                                       @Param("email") String email){
        return adminUserService.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }
}
