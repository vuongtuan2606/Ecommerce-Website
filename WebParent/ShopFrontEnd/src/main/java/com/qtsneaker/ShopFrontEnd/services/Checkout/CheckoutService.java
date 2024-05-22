package com.qtsneaker.ShopFrontEnd.services.Checkout;

import com.qtsneaker.common.entity.Cart;

import java.util.List;

public interface CheckoutService {

    CheckoutInfo prepareCheckout(List<Cart> cartItems);

    float calculateProductTotal(List<Cart> cartItems);
    float calculateProductCost(List<Cart> cartItems);
}
