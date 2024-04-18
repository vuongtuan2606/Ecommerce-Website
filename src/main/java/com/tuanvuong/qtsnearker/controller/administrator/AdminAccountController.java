package com.tuanvuong.qtsnearker.controller.administrator;


import com.tuanvuong.qtsnearker.entity.User;
import com.tuanvuong.qtsnearker.security.AdminUserDetails;

import com.tuanvuong.qtsnearker.services.UserService;
import com.tuanvuong.qtsnearker.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AdminAccountController  {
    @Autowired
    private UserService userService;

    /* @AuthenticationPrincipal
    *  là một annotation trong Spring Security
    *  được sử dụng để truy cập vào thông tin người dùng
    *  đã được xác thực (authenticated) trong một phương thức controller hoặc service.
    * */
    @GetMapping("/admin/account")
    public String viewDetails(@AuthenticationPrincipal AdminUserDetails loggedUser, Model model){

        // lấy tên  người dùng đăng nhập
        String email = loggedUser.getUsername();

        //truyền vào email của người dùng đang đăng nhập để lấy thông tin đầy đủ về người dùng từ cơ sở dữ liệu.
        User user = userService.getByEmail(email);

        model.addAttribute("user", user);

        return "administrator/users-profile";
    }

    @PostMapping("/admin/account/update")
    public String saveUser(User user,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal AdminUserDetails loggedUser,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            user.setPhotos(fileName);

            User savedUser = userService.updateAccount(user);

            // đường dẫn thư mục ->  tạo thư mục "user-photos"
            String uploadDir ="user-photos/" +savedUser.getId();

            // xóa ảnh cũ
            FileUploadUtil.cleanDir(uploadDir);

            // lưu tệp vào mục chỉ định
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else {
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
        }

        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());
        loggedUser.setPhoto(user.getPhotos());

        userService.updateAccount(user);
        redirectAttributes.addFlashAttribute("message","Your account detail have been updated");

        return "redirect:/admin/account";

    }
}
