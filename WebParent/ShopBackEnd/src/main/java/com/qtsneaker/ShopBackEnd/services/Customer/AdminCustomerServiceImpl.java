package com.qtsneaker.ShopBackEnd.services.Customer;


import com.qtsneaker.ShopBackEnd.dao.AdminCustomerRepository;
import com.qtsneaker.common.entity.Customer;

import com.qtsneaker.common.exception.CustomerNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Transactional
public class AdminCustomerServiceImpl implements AdminCustomerService {

    @Autowired
    private AdminCustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMERS_PER_PAGE, sort);

        if (keyword != null) {
            return customerRepository.findAll(keyword, pageable);
        }

        return customerRepository.findAll(pageable);
    }

    @Override
    public Customer get(Integer id) throws CustomerNotFoundException {

        try{
            return customerRepository.findById(id).get();
        }
        catch (NoSuchElementException ex){
            throw  new CustomerNotFoundException("Không tìm thấy khách hàng có id" + id);
        }

    }

    @Override
    public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
        customerRepository.updateEnabledStatus(id, enabled);
    }

    @Override
    public void delete(Integer id) throws CustomerNotFoundException {
        Long count = customerRepository.countById(id);
        if (count == null || count == 0) {
            throw new CustomerNotFoundException("Không tìm thấy khách hàng có id " + id);
        }

        customerRepository.deleteById(id);
    }

}
