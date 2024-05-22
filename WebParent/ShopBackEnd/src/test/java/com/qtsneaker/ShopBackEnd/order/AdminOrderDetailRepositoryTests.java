package com.qtsneaker.ShopBackEnd.order;

import com.qtsneaker.ShopBackEnd.ShopBackEndApplication;
import com.qtsneaker.ShopBackEnd.dao.AdminOrderDetailRepository;
import com.qtsneaker.ShopBackEnd.dao.AdminOrderRepository;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.Product;
import com.qtsneaker.common.entity.order.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {ShopBackEndApplication.class})
@DataJpaTest(showSql = false)
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminOrderDetailRepositoryTests {
    @Autowired private AdminOrderDetailRepository repo;

    @Test
    public void testFindWithCategoryAndTimeBetween() throws ParseException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = dateFormatter.parse("2024-05-01");
        Date endTime = dateFormatter.parse("2024-05-30");

        List<OrderDetail> listOrderDetails = repo.findWithCategoryAndTimeBetween(startTime, endTime);

        assertThat(listOrderDetails.size()).isGreaterThan(0);

        for (OrderDetail detail : listOrderDetails) {
            System.out.printf("%-30s | %d | %10.2f| %10.2f | %10.2f \n",
                    detail.getProduct().getCategory().getName(),
                    detail.getQuantity(), detail.getProductCost(),
                    detail.getSubtotal());
        }
    }

    @Test
    public void testFindWithProductAndTimeBetween() throws ParseException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = dateFormatter.parse("2024-05-01");
        Date endTime = dateFormatter.parse("2024-05-30");

        List<OrderDetail> listOrderDetails = repo.findWithProductAndTimeBetween(startTime, endTime);

        assertThat(listOrderDetails.size()).isGreaterThan(0);

        for (OrderDetail detail : listOrderDetails) {
            System.out.printf("%-70s | %d | %10.2f| %10.2f | %10.2f \n",
                    detail.getProduct().getShortName(),
                    detail.getQuantity(), detail.getProductCost(),
                    detail.getSubtotal());
        }
    }

    @Test
    public void testFindOrderDetailAndTimeBetween() throws ParseException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = dateFormatter.parse("2024-05-01");
        Date endTime = dateFormatter.parse("2024-05-30");

        Pageable pageable = PageRequest.of(0, 5);
        List<OrderDetail> listOrderDetails = repo.findOrderDetailsByOrderTimeBetween(startTime, endTime, pageable);

        assertThat(listOrderDetails.size()).isGreaterThan(0);

        for (OrderDetail detail : listOrderDetails) {
            System.out.printf("Order Detail ID: %d, Product ID: %d, Quantity: %d, Unit Price: %10.2f, Subtotal: %10.2f, Order ID: %d, Order Time: %s, Status: %s\n",
                    detail.getId(),
                    detail.getProduct().getId(),
                    detail.getQuantity(),
                    detail.getUnitPrice(),
                    detail.getSubtotal(),
                    detail.getOrder().getId(),
                    detail.getOrder().getOrderTime(),
                    detail.getOrder().getStatus());
        }
    }

}
