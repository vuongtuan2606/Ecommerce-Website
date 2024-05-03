package com.tuanvuong.qtsnearker.services.administrator;

import com.tuanvuong.qtsnearker.entity.Customer;
import com.tuanvuong.qtsnearker.services.exceptions.CustomerNotFoundException;
import org.springframework.data.domain.Page;

public interface AdminCustomerService {
    public static final int CUSTOMERS_PER_PAGE = 5;
    Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword);
    Customer get(Integer id) throws CustomerNotFoundException;
    void updateCustomerEnabledStatus(Integer id, boolean enabled);
    void delete(Integer id) throws CustomerNotFoundException;


}
