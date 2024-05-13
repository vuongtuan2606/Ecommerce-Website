package com.qtsneaker.ShopFrontEnd.services.Address;

import com.qtsneaker.ShopFrontEnd.dao.AddressRepository;
import com.qtsneaker.common.entity.Address;
import com.qtsneaker.common.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired private AddressRepository addressRepository;

    /*lấy danh sách địa chỉ của khách hàng*/
    @Override
    public List<Address> listAddressBook(Customer customer) {
        return addressRepository.findByCustomer(customer);
    }

    /*Lưu địa chỉ*/
    @Override
    public void save(Address address) {
        addressRepository.save(address);
    }

    /*Lấy địa chỉ được chọn theo addressId và customerId*/
    @Override
    public Address get(Integer addressId, Integer customerId) {
        return addressRepository.findByIdAndCustomer(addressId, customerId);
    }

    /*Xóa 1 địa chỉ được chọn*/
    @Override
    public void delete(Integer addressId, Integer customerId) {
            addressRepository.deleteByIdAndCustomer(addressId,customerId);
    }

    /* Set địa chỉ mặc định */
    @Override
    public void setDefaultAddress(Integer defaultAddressId, Integer customerId) {
        // Kiểm tra xem defaultAddressId có hợp lệ không
        if (defaultAddressId > 0) {
            // Nếu defaultAddressId hợp lệ, gọi phương thức setDefaultAddress để đặt địa chỉ mặc định
            addressRepository.setDefaultAddress(defaultAddressId);
        }

        // Sau khi đặt địa chỉ mặc định, đặt các địa chỉ khác của khách hàng thành không phải là mặc định
        // Đảm bảo rằng chỉ có một địa chỉ được đặt là mặc định cho khách hàng
        addressRepository.setNonDefaultForOthers(defaultAddressId, customerId);
    }


    /*Lấy địa chỉ mặc định cửa khách hàng*/
    @Override
    public Address getDefaultAddress(Customer customer) {
        return addressRepository.findDefaultByCustomer(customer.getId());
    }

}
