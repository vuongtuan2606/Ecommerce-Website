package com.qtsneaker.ShopFrontEnd.services.Order;

import com.qtsneaker.ShopFrontEnd.services.Checkout.CheckoutInfo;
import com.qtsneaker.common.entity.Address;
import com.qtsneaker.common.entity.Cart;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.order.Order;
import com.qtsneaker.common.entity.order.PaymentMethod;

import java.util.List;

public interface OrderService {
    Order createOrder(Customer customer, Address address, List<Cart> cartItems,
                      PaymentMethod paymentMethod, CheckoutInfo checkoutInfo);
}
