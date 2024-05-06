package com.qtsneaker.ShopFrontEnd.services;

import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.exception.CustomerNotFoundException;
import org.springframework.data.domain.Page;

public interface AdminCustomerService {
    public static final int CUSTOMERS_PER_PAGE = 5;
    Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword);
    Customer get(Integer id) throws CustomerNotFoundException;
    void updateCustomerEnabledStatus(Integer id, boolean enabled);
    void delete(Integer id) throws CustomerNotFoundException;


}
