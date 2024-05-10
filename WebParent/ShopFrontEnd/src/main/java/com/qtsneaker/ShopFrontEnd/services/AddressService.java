package com.qtsneaker.ShopFrontEnd.services;

import com.qtsneaker.common.entity.Address;
import com.qtsneaker.common.entity.Customer;

import java.util.List;

public interface AddressService {

    List<Address> listAddressBook(Customer customer);
    void save(Address address);
    Address get(Integer addressId, Integer customerId);

    void delete(Integer addressId, Integer customerId);

    void setDefaultAddress(Integer defaultAddressId, Integer customerId);

    Address getDefaultAddress(Customer customer);

}
