package com.qtsneaker.ShopBackEnd.services.Order;


import com.qtsneaker.ShopBackEnd.exception.OrderNotFoundException;
import com.qtsneaker.common.entity.Product;
import com.qtsneaker.common.entity.order.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminOrderService {
    public  static final int ORDERS_PER_PAGE = 10;
    List<Order> ListProductAll();
    Page<Order> listByPage(int pageNum, String sortField, String sortDir, String keyword);

    Order get(Integer id) throws OrderNotFoundException;

    void updateStatus(Integer orderId, String status);

    Long getTotalToday();
    Long getTotalLast7Days();
    Long getTotalLastMonth();
}
