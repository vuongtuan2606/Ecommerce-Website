package com.tuanvuong.qtsnearker.controller.administrator;

import com.tuanvuong.qtsnearker.entity.Role;
import com.tuanvuong.qtsnearker.entity.User;
import com.tuanvuong.qtsnearker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private UserService userService;


    @GetMapping("/users")
    public String listAll(Model theModel){

        List<User> listUsers = userService.findUserAll();

        theModel.addAttribute("listUsers",listUsers);

        return "administrator/users";
    }
    @GetMapping("/users/create")
    public String newUser(Model model){

        User user = new User();

        List<Role> listRoles = userService.findRoleAll();

        user.setEnabled(true);

        model.addAttribute("newUser", user);

        model.addAttribute("listRoles", listRoles);

        return "administrator/users_form";
    }


    /* redirectAttributes.addFlashAttribute("key", "value")
    * khi truyền thông điệp hoặc dữ liệu từ một request tới một redirect request
    * khi thêm một flash attribute, dữ liệu đó sẽ được lưu trữ tạm thời trong session
    * và chỉ tồn tại cho một request redirect duy nhất.
    * Sau khi request redirect hoàn tất, dữ liệu flash attribute sẽ bị xóa.
    * */
    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes){

        userService.save(user);

        // Nếu thêm thành công, thì thêm flash attribute để hiển thị thông báo
        redirectAttributes.addFlashAttribute("message","Thêm người dùng thành công");

        return "redirect:/admin/users";
    }

}
