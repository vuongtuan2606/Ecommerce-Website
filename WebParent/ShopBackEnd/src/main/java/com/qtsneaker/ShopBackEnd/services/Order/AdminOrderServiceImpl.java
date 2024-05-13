package com.qtsneaker.ShopBackEnd.services.Order;

import com.qtsneaker.ShopBackEnd.dao.AdminOrderRepository;
import com.qtsneaker.ShopBackEnd.exception.OrderNotFoundException;
import com.qtsneaker.common.entity.order.Order;
import com.qtsneaker.common.entity.order.OrderStatus;
import com.qtsneaker.common.entity.order.OrderTrack;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AdminOrderServiceImpl implements AdminOrderService {

    @Autowired private AdminOrderRepository repo;


    @Override
    public List<Order> ListProductAll() {
        return repo.findAll();
    }

    @Override
    public Page<Order> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = null;

        if ("destination".equals(sortField)) {
            sort = Sort.by("province").and(Sort.by("district"));
        } else {
            sort = Sort.by(sortField);
        }

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

        Page<Order> page = null;

        if (keyword != null) {
            page = repo.findAll(keyword, pageable);
        }

        return repo.findAll(pageable);
    }

    @Override
    public Order get(Integer id) throws OrderNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new OrderNotFoundException("Could not find any orders with ID " + id);
        }
    }


    @Override
    public void updateStatus(Integer orderId, String status) {
        Order orderInDB = repo.findById(orderId).get();
        OrderStatus statusToUpdate = OrderStatus.valueOf(status);

        if (!orderInDB.hasStatus(statusToUpdate)) {
            List<OrderTrack> orderTracks = orderInDB.getOrderTracks();

            OrderTrack track = new OrderTrack();
            track.setOrder(orderInDB);
            track.setStatus(statusToUpdate);
            track.setUpdatedTime(new Date());
            track.setNotes(statusToUpdate.defaultDescription());

            orderTracks.add(track);

            orderInDB.setStatus(statusToUpdate);

            repo.save(orderInDB);
        }
    }
}
