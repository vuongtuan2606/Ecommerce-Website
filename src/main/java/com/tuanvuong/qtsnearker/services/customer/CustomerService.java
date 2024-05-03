package com.tuanvuong.qtsnearker.services.customer;

import com.tuanvuong.qtsnearker.entity.Customer;

public interface CustomerService {
    boolean isEmailUnique(String email);

    void registerCustomer(Customer customer);

    void encodePassword(Customer customer);

    boolean verify(String verificationCode);

}
