package com.qtsneaker.ShopFrontEnd.services.Order;

import com.qtsneaker.ShopFrontEnd.dao.OrderRepository;
import com.qtsneaker.ShopFrontEnd.services.Checkout.CheckoutInfo;
import com.qtsneaker.common.entity.Address;
import com.qtsneaker.common.entity.Cart;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.Product;
import com.qtsneaker.common.entity.order.Order;
import com.qtsneaker.common.entity.order.OrderDetail;
import com.qtsneaker.common.entity.order.OrderStatus;
import com.qtsneaker.common.entity.order.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired private OrderRepository repository;

    @Override
    public Order createOrder(Customer customer, Address address, List<Cart> cartItems, PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
        Order newOrder = new Order();

        newOrder.setOrderTime(new Date());
        newOrder.setStatus(OrderStatus.NEW);

        newOrder.setCustomer(customer);
        newOrder.copyAddressFromOrder(address);

        newOrder.setPaymentMethod(paymentMethod);

        newOrder.setSubtotal(checkoutInfo.getProductTotal());
        newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
        newOrder.setTotal(checkoutInfo.getPaymentTotal());

        Set<OrderDetail> orderDetails = newOrder.getOrderDetails();


        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setUnitPrice(product.getDiscountPrice());
            orderDetail.setSubtotal(cartItem.getSubtotal());
            orderDetails.add(orderDetail);
        }
        return repository.save(newOrder);
    }
}
