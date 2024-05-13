package com.qtsneaker.ShopFrontEnd.controller;

import com.qtsneaker.ShopFrontEnd.services.Address.AddressService;
import com.qtsneaker.ShopFrontEnd.services.Cart.CartService;
import com.qtsneaker.common.entity.Cart;
import com.qtsneaker.common.entity.Customer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ControllerHelper controllerHelper;

    @Autowired private CartService cartService;

    @Autowired private AddressService addressService;


    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) {

        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        List<Cart> cartItems = cartService.listCartItems(customer);

        float estimatedTotal = 0.0F;

        for (Cart item : cartItems) {
            estimatedTotal += item.getSubtotal();
        }

        model.addAttribute("cartItems", cartItems);

        model.addAttribute("estimatedTotal", estimatedTotal);

        return "cart/cart_list";
    }
}
