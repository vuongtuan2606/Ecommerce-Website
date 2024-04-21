package com.tuanvuong.qtsnearker.rest.administrator;

import com.tuanvuong.qtsnearker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminUserRestController {
    @Autowired
    private UserService userService;
    @PostMapping("/users/check_email")
    public  String checkDuplicateEmail(@Param("id") Integer id,
                                       @Param("email") String email){
        return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }
}