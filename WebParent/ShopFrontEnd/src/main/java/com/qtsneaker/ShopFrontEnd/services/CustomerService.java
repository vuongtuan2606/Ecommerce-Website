package com.qtsneaker.ShopFrontEnd.services;


import com.qtsneaker.common.entity.AuthenticationType;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.exception.CustomerNotFoundException;

public interface CustomerService {
    boolean isEmailUnique(String email);

    void registerCustomer(Customer customer);

    void encodePassword(Customer customer);

    boolean verify(String verificationCode);

    void updateAuthenticationType(Customer customer, AuthenticationType type);

    Customer getCustomerByEmail(String email);

    void addNewCustomerUponOAuthLogin(String name, String email,AuthenticationType authenticationType);

     void update(Customer customerInForm);

    String updateResetPasswordToken(String email) throws CustomerNotFoundException;

    Customer getByResetPasswordToken(String token);
    void updatePassword(String token, String newPassword) throws CustomerNotFoundException;
}
