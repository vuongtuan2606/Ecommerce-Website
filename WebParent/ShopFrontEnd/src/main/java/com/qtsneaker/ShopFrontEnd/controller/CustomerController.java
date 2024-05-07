package com.qtsneaker.ShopFrontEnd.controller;


import com.qtsneaker.ShopFrontEnd.security.CustomerUserDetails;
import com.qtsneaker.ShopFrontEnd.security.oauth.CustomerOAuth2User;
import com.qtsneaker.ShopFrontEnd.services.CustomerService;
import com.qtsneaker.ShopFrontEnd.services.SettingService;
import com.qtsneaker.ShopFrontEnd.setting.EmailSettingBag;
import com.qtsneaker.ShopFrontEnd.util.Utility;
import com.qtsneaker.common.entity.Customer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        Customer customers = new Customer();

        model.addAttribute("pageTitle", "Đăng ký khách hàng");
        model.addAttribute("customers", customers);

        return "register/register_form";
    }
    @PostMapping("/register/success")
    public String createCustomer(Customer customers,
                                 Model model,
                                 HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        customerService.registerCustomer(customers);

        sendVerificationEmail(request, customers);

        model.addAttribute("pageTitle", "Đăng ký thành công!");

        return "register/register_success";
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
        return "register/" + (verified ? "verify_success" : "verify_fail");
    }

    @GetMapping("/account_details")
    public String viewAccountDetails(Model model, HttpServletRequest request) {
        // Lấy địa chỉ email của khách hàng đã xác thực từ request
        String email = Utility.getEmailOfAuthenticatedCustomer(request);

        // Lấy thông tin khách hàng dựa trên địa chỉ email
        Customer customer = customerService.getCustomerByEmail(email);

        model.addAttribute("customer", customer);

        return "customer/account_detail";
    }

    @PostMapping("/update_account_details")
    public String updateAccountDetails(Model model,
                                       Customer customer,
                                       RedirectAttributes redirectAttributes,
                                       HttpServletRequest request) {

        customerService.update(customer);

        redirectAttributes.addFlashAttribute("message", "Tài khoản của bạn đã được cập nhật.");

        updateNameForAuthenticatedCustomer(customer, request);

        return "redirect:/account_details";
    }

    /**
     * Cập nhật tên cho khách hàng đã xác thực.
     *
     * @param customer Đối tượng Customer chứa thông tin tên mới của khách hàng.
     * @param request  Đối tượng HttpServletRequest chứa thông tin về request hiện tại.
     */
    private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
        // Lấy principal từ request
        Object principal = request.getUserPrincipal();

        // Xác định loại xác thực và cập nhật tên tương ứng
        if (principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
            // Nếu sử dụng xác thực username/password hoặc remember-me

            CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);

            Customer authenticatedCustomer = userDetails.getCustomer();

            authenticatedCustomer.setFirstName(customer.getFirstName());

            authenticatedCustomer.setLastName(customer.getLastName());

        } else if (principal instanceof OAuth2AuthenticationToken) {
            // Nếu sử dụng xác thực OAuth2

            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;

            CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();

            String fullName = customer.getFirstName() + " " + customer.getLastName();

            oauth2User.setFullName(fullName);
        }
    }

    /**
     * Lấy đối tượng CustomerUserDetails từ principal.
     *
     * @param principal Đối tượng principal chứa thông tin về người dùng đã xác thực.
     * @return Đối tượng CustomerUserDetails tương ứng với principal.
     */
    private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {

        CustomerUserDetails userDetails = null;

        if (principal instanceof UsernamePasswordAuthenticationToken) {

            // Nếu principal là UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;

            userDetails = (CustomerUserDetails) token.getPrincipal();

        } else if (principal instanceof RememberMeAuthenticationToken) {

            // Nếu principal là RememberMeAuthenticationToken
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;

            userDetails = (CustomerUserDetails) token.getPrincipal();
        }

        return userDetails;
    }

}

