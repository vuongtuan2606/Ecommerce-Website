package com.tuanvuong.qtsnearker.controller.administrator;

import com.tuanvuong.qtsnearker.entity.Role;
import com.tuanvuong.qtsnearker.entity.User;
import com.tuanvuong.qtsnearker.services.UserNotFoundException;
import com.tuanvuong.qtsnearker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

 @Controller
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private UserService userService;


    @GetMapping("/users")
    public String listAll(Model theModel){

        List<User> user = userService.findUserAll();

        theModel.addAttribute("user",user);

        return "administrator/users";
    }
    @GetMapping("/users/create")
    public String newUser(Model model){

        User user = new User();

        List<Role> listRoles = userService.findRoleAll();

        user.setEnabled(true);

        model.addAttribute("user", user);

        model.addAttribute("listRoles", listRoles);

        model.addAttribute("pageTitle","Thêm mới");

        return "administrator/users_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes){
        userService.save(user);
        redirectAttributes.addFlashAttribute("message","The user has been saved successfully");
        return "redirect:/admin/users";

    }


    /* redirectAttributes.addFlashAttribute("key", "value")
    * khi truyền thông điệp hoặc dữ liệu từ một request tới một redirect request
    * khi thêm một flash attribute, dữ liệu đó sẽ được lưu trữ tạm thời trong session
    * và chỉ tồn tại cho một request redirect duy nhất.
    * Sau khi request redirect hoàn tất, dữ liệu flash attribute sẽ bị xóa.
    * */

     @GetMapping ("/users/edit/{id}")
     public String editUser(@PathVariable(name = "id") Integer id,
                            Model model,
                            RedirectAttributes redirectAttributes )  {
         try{
             User user =userService.findById(id);

             List<Role> listRoles = userService.findRoleAll();

             model.addAttribute("user", user);

             model.addAttribute("listRoles", listRoles);

             model.addAttribute("pageTitle","Cập nhật (ID: "+id+")");
         }
         catch (UserNotFoundException ex){
             redirectAttributes.addFlashAttribute("message",ex.getMessage());
         }

         return "administrator/users_form";
     }



}
