package com.qtsneaker.ShopFrontEnd.controller;


import com.qtsneaker.ShopFrontEnd.dao.ProvinceRepository;
import com.qtsneaker.ShopFrontEnd.security.CustomerUserDetails;
import com.qtsneaker.ShopFrontEnd.security.oauth.CustomerOAuth2User;
import com.qtsneaker.ShopFrontEnd.services.Address.AddressService;
import com.qtsneaker.ShopFrontEnd.services.Customer.CustomerService;
import com.qtsneaker.ShopFrontEnd.services.Order.OrderService;
import com.qtsneaker.ShopFrontEnd.services.Setting.SettingService;
import com.qtsneaker.ShopFrontEnd.setting.EmailSettingBag;
import com.qtsneaker.ShopFrontEnd.util.Utility;
import com.qtsneaker.common.entity.Address;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.Province;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired private CustomerService customerService;
    @Autowired private SettingService settingService;
    @Autowired private ControllerHelper controllerHelper;
    @Autowired private AddressService addressService;
    @Autowired private ProvinceRepository provinceRepository;
    @Autowired private OrderService orderService;

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
        mailSender.setDefaultEncoding("utf-8");

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
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        List<Province> listProvince = customerService.listAllProvince();

        // Lấy danh sách địa chỉ của khách hàng tương ứng
        List<Address> listAddresses = addressService.listAddressBook(customer);

        model.addAttribute("customer", customer);
        model.addAttribute("listAddresses", listAddresses);
        model.addAttribute("listProvince",listProvince);
        model.addAttribute("pageTitle", "Thông tin tài khoản");
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


    /*Addresses*/
    @GetMapping("/account_details/address_book/new")
    public String newAddress(Model model) {
        List<Province> listProvince = provinceRepository.findAllByOrderByNameAsc();

        model.addAttribute("listProvince", listProvince);
        model.addAttribute("address", new Address());
        model.addAttribute("pageTitle", "Thêm địa chỉ mới");

        return "customer/address_form";
    }

    @PostMapping("/account_details/address_book/save")
    public String saveAddress(Address address,
                              HttpServletRequest request,
                              RedirectAttributes ra) {

        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        // Lấy danh sách các địa chỉ của khách hàng
        List<Address> customerAddresses = addressService.listAddressBook(customer);

        // Kiểm tra xem đã có địa chỉ mặc định nào được thiết lập cho khách hàng trước đó hay chưa
        boolean hasDefaultAddress = customerAddresses.stream()
                .anyMatch(Address::isDefaultForShipping);

        // Kiểm tra xem có phải là địa chỉ mới hay không
        boolean isNewAddress = address.getId() == null;

        // Nếu là địa chỉ mới và chưa có địa chỉ mặc định nào được thiết lập trước đó
        if (isNewAddress && !hasDefaultAddress) {
            // Thiết lập địa chỉ mặc định cho vận chuyển
            address.setDefaultForShipping(true);
        }

        address.setCustomer(customer);

        addressService.save(address);

        ra.addFlashAttribute("message", "Địa chỉ của bạn đã được lưu thành công !.");

        return "redirect:/account_details";
    }
    @GetMapping("/account_details/address_book/edit/{id}")
    public String editAddress(@PathVariable("id") Integer addressId,
                              Model model,
                              HttpServletRequest request) {

        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        List<Province> listProvince = provinceRepository.findAllByOrderByNameAsc();

        Address address = addressService.get(addressId, customer.getId());

        model.addAttribute("address", address);
        model.addAttribute("listProvince", listProvince);
        model.addAttribute("pageTitle", "Chỉnh sửa địa chỉ (ID: " + addressId + ")");

        return "customer/address_form";
    }

    @GetMapping("/account_details/address_book/delete/{id}")
    public String deleteAddress(@PathVariable("id") Integer addressId,
                                RedirectAttributes ra,
                                HttpServletRequest request) {

        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        addressService.delete(addressId, customer.getId());

        ra.addFlashAttribute("message", "Địa chỉ có id: " + addressId + " đã được xóa thành công.");

         return "redirect:/account_details";
    }

    @GetMapping("/account_details/address_book/default/{id}")
    public String setDefaultAddress(@PathVariable("id") Integer addressId,
                                    RedirectAttributes ra,
                                    HttpServletRequest request) {

        Customer customer = controllerHelper.getAuthenticatedCustomer(request);
        // Lấy danh sách địa chỉ của khách hàng tương ứng
        List<Address> listAddresses = addressService.listAddressBook(customer);

        addressService.setDefaultAddress(addressId, customer.getId());
        ra.addFlashAttribute("message", "Đã thay đổi địa chỉ mặc định.");
        return "redirect:/account_details";

    }
}

