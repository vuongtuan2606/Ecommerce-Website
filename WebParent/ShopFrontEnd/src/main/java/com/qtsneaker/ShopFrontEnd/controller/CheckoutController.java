package com.qtsneaker.ShopFrontEnd.controller;

import com.qtsneaker.ShopFrontEnd.services.Address.AddressService;
import com.qtsneaker.ShopFrontEnd.services.Cart.CartService;
import com.qtsneaker.ShopFrontEnd.services.Checkout.CheckoutInfo;
import com.qtsneaker.ShopFrontEnd.services.Checkout.CheckoutService;
import com.qtsneaker.ShopFrontEnd.services.Order.OrderService;
import com.qtsneaker.ShopFrontEnd.services.Setting.SettingService;
import com.qtsneaker.ShopFrontEnd.setting.CurrencySettingBag;
import com.qtsneaker.ShopFrontEnd.setting.EmailSettingBag;
import com.qtsneaker.ShopFrontEnd.util.Utility;
import com.qtsneaker.common.entity.Address;
import com.qtsneaker.common.entity.Cart;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.order.Order;
import com.qtsneaker.common.entity.order.OrderDetail;
import com.qtsneaker.common.entity.order.PaymentMethod;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class CheckoutController {
    @Autowired private  ControllerHelper helper;

    @Autowired private AddressService addressService;

    @Autowired private CartService cartService;

    @Autowired private CheckoutService checkoutService;

    @Autowired private OrderService orderService;

    @Autowired private SettingService settingService;

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest request) {

        Customer customer = helper.getAuthenticatedCustomer(request);

        Address defaultAddress = addressService.getDefaultAddress(customer);

        List<Cart> cartItems = cartService.listCartItems(customer);

        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems);

        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("defaultAddress",defaultAddress);
        model.addAttribute("cartItems", cartItems);

        return "checkout/checkout";
    }
    @PostMapping("/place_order")
    public String placeOrder(HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        String paymentType = request.getParameter("paymentMethod");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

        Customer customer = helper.getAuthenticatedCustomer(request);

        Address defaultAddress = addressService.getDefaultAddress(customer);


        List<Cart> cartItems = cartService.listCartItems(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems);

        Order createdOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);

        // xóa thông tin khách hàng khỏi giỏ hàng
        cartService.deleteByCustomer(customer);

        sendOrderConfirmationEmail(request, createdOrder);

        return "checkout/order_completed";
    }

    private void sendOrderConfirmationEmail(HttpServletRequest request, Order order)
            throws UnsupportedEncodingException, MessagingException {
        // Lấy  email settings từ service
        EmailSettingBag emailSettings = settingService.getEmailSettings();

        // Chuẩn bị mail sender
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
        mailSender.setDefaultEncoding("utf-8");

        // Trích xuất thông tin email
        String toAddress = order.getCustomer().getEmail();
        String subject = emailSettings.getOrderConfirmationSubject();
        String content = emailSettings.getOrderConfirmationContent();

        subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        DateFormat dateFormatter =  new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
        String orderTime = dateFormatter.format(order.getOrderTime());

        CurrencySettingBag currencySettings = settingService.getCurrencySettings();
        String totalAmount = Utility.formatCurrency(order.getTotal(), currencySettings);

        content = content.replace("[[name]]", order.getCustomer().getFullName());
        content = content.replace("[[orderId]]", String.valueOf(order.getId()));
        content = content.replace("[[orderTime]]", orderTime);
        content = content.replace("[[shippingAddress]]", order.getDestination());
        content = content.replace("[[total]]", totalAmount);
        content = content.replace("[[paymentMethod]]", order.getPaymentMethod().toString());
        content = content.replace("[[orderLink]]", "http://localhost:8080/login");

        helper.setText(content, true);
        mailSender.send(message);
    }
}
