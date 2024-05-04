package com.qtsneaker.ShopFrontEnd.controller;


import com.qtsneaker.ShopFrontEnd.services.CustomerService;
import com.qtsneaker.ShopFrontEnd.services.SettingService;
import com.qtsneaker.ShopFrontEnd.setting.EmailSettingBag;
import com.qtsneaker.ShopFrontEnd.setting.Utility;
import com.qtsneaker.common.entity.Customer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        model.addAttribute("pageTitle", "Đăng ký khách hàng");
        model.addAttribute("customer", new Customer());

        return "/register/register_form";


    }
    @PostMapping("/create_customer")
    public String createCustomer(Customer customer,
                                 Model model,
                                 HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        customerService.registerCustomer(customer);

        sendVerificationEmail(request, customer);

        model.addAttribute("pageTitle", "Đăng ký thành công!");

        return "/register/register_succses";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer)
            throws UnsupportedEncodingException, MessagingException {

        // Lấy  email settings từ service
        EmailSettingBag emailSettings = settingService.getEmailSettings();

        // Chuẩn bị mail sender
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        // Trích xuất thông tin email
        String toAddress = customer.getEmail();
        String subject = emailSettings.getCustomerVerifySubject();
        String content = emailSettings.getCustomerVerifyContent();

        // Tạo MimeMessage và MimeMessageHelper
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        // Thiết lập người gửi ,người nhận và chủ đề
        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        // Thay thế các placeholder trong nội dung với thông tin cụ thể của khách hàng
        content = content.replace("[[name]]", customer.getFullName());
        String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        // Thiết lập nội dung email
        helper.setText(content, true);

        // Gửi email
        mailSender.send(message);


        System.out.println("to Address: " + toAddress);
        System.out.println("Verify URL: " + verifyURL);
    }




    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model model) {

        // Xác nhận mã xác nhận của khách hàng
        boolean verified = customerService.verify(code);

        // Trả về view tương ứng với kết quả xác nhận
        return "/register/" + (verified ? "verify_succses" : "verify_fail");
    }
}

