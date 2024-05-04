package com.qtsneaker.ShopFrontEnd.services;

import com.qtsneaker.common.entity.Customer;

public interface CustomerService {
    boolean isEmailUnique(String email);

    void registerCustomer(Customer customer);

    void encodePassword(Customer customer);

    boolean verify(String verificationCode);

}