package com.qtsneaker.ShopFrontEnd.services.Order;

import com.qtsneaker.ShopFrontEnd.dao.OrderRepository;
import com.qtsneaker.ShopFrontEnd.services.Checkout.CheckoutInfo;
import com.qtsneaker.common.entity.Address;
import com.qtsneaker.common.entity.Cart;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.Product;
import com.qtsneaker.common.entity.order.*;
import com.qtsneaker.common.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired private OrderRepository repository;

    @Override
    public Order createOrder(Customer customer, Address address, List<Cart> cartItems,
                             PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
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
            orderDetail.setSize(cartItem.getProductSize());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setUnitPrice(product.getDiscountPrice());
            orderDetail.setSubtotal(cartItem.getSubtotal());
            orderDetail.setShippingCost(checkoutInfo.getShippingCostTotal());
            orderDetails.add(orderDetail);
        }

        OrderTrack track = new OrderTrack();
        track.setOrder(newOrder);
        track.setStatus(OrderStatus.NEW);
        track.setNotes(OrderStatus.NEW.defaultDescription());
        track.setUpdatedTime(new Date());

        newOrder.getOrderTracks().add(track);

        return repository.save(newOrder);
    }

    @Override
    public Page<Order> listForCustomerByPage(Customer customer, int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

        if (keyword != null) {
            return repository.findAll(keyword, customer.getId(), pageable);
        }

        return repository.findAll(customer.getId(), pageable);
    }

    @Override
    public Order getOrder(Integer id, Customer customer) {
        return repository.findByIdAndCustomer(id, customer);
    }




}
