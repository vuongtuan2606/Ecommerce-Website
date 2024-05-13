package com.qtsneaker.ShopBackEnd.controller;


import com.qtsneaker.ShopBackEnd.security.AdminUserDetails;
import com.qtsneaker.ShopBackEnd.services.User.AdminUserService;
import com.qtsneaker.ShopBackEnd.util.FileUploadUtil;

import com.qtsneaker.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminAccountController  {
    @Autowired
    private AdminUserService adminUserService;

    /* @AuthenticationPrincipal
    *  là một annotation trong Spring Security
    *  được sử dụng để truy cập vào thông tin người dùng
    *  đã được xác thực (authenticated) trong một phương thức controller hoặc service.
    * */
    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal AdminUserDetails loggedUser, Model model){

        // lấy tên  người dùng đăng nhập
        String email = loggedUser.getUsername();

        //truyền vào email của người dùng đang đăng nhập để lấy thông tin đầy đủ về người dùng từ cơ sở dữ liệu.
        User user = adminUserService.getByEmail(email);

        model.addAttribute("user", user);

        return "/user/users-profile";
    }

    @PostMapping("/account/update")
    public String saveUser(User user,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal AdminUserDetails loggedUser,
                           @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            user.setPhotos(fileName);

            User savedUser = adminUserService.updateAccount(user);

            // đường dẫn thư mục ->  tạo thư mục "user-photos"
            String uploadDir = "../user-photos/" + savedUser.getId();

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

        adminUserService.updateAccount(user);
        redirectAttributes.addFlashAttribute("message","Your account detail have been updated");

        return "redirect:/admin/account";

    }
}
