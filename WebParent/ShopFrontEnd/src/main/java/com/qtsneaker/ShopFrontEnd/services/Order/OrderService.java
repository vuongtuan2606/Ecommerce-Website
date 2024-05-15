package com.qtsneaker.ShopFrontEnd.services.Order;

import com.qtsneaker.ShopFrontEnd.services.Checkout.CheckoutInfo;
import com.qtsneaker.common.entity.Address;
import com.qtsneaker.common.entity.Cart;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.order.Order;
import com.qtsneaker.common.entity.order.PaymentMethod;
import com.qtsneaker.common.exception.OrderNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    public static final int ORDERS_PER_PAGE = 5;
    Order createOrder(Customer customer, Address address, List<Cart> cartItems,
                      PaymentMethod paymentMethod, CheckoutInfo checkoutInfo);

    Page<Order> listForCustomerByPage(Customer customer, int pageNum,
                                      String sortField, String sortDir, String keyword);

    Order getOrder(Integer id, Customer customer);



}
