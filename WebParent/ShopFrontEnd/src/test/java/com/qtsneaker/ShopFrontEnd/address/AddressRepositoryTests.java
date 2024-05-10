package com.qtsneaker.ShopFrontEnd.address;

import com.qtsneaker.ShopFrontEnd.ShopFrontEndApplication;
import com.qtsneaker.ShopFrontEnd.dao.AddressRepository;
import com.qtsneaker.common.entity.Address;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.Province;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {ShopFrontEndApplication.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Rollback(false)
public class AddressRepositoryTests {

    @Autowired private AddressRepository repository;

    @Test
    public void testAddNew(){
        Integer customerId = 33;
        Integer provinceId = 1; // Hà nội

        Address newAddress = new Address();

        newAddress.setCustomer(new Customer(customerId));
        newAddress.setProvince(new Province(provinceId));
        newAddress.setFirstName("tuấn");
        newAddress.setLastName("Vương");
        newAddress.setPhoneNumber("0899263162");
        newAddress.setAddressLine1("8, Ngọa long ");
        newAddress.setDistrict("Bac tu liem");

        Address savedAddress = repository.save(newAddress);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isGreaterThan(0);
    }
    @Test
    public void testAddNew2(){
        Integer customerId = 33;
        Integer provinceId = 4; // Hà nội

        Address newAddress = new Address();

        newAddress.setCustomer(new Customer(customerId));
        newAddress.setProvince(new Province(provinceId));
        newAddress.setFirstName("tuấn");
        newAddress.setLastName("Vương");
        newAddress.setPhoneNumber("0899263162");
        newAddress.setAddressLine1("8, Ngọa long ");
        newAddress.setDistrict("minh khai");

        Address savedAddress = repository.save(newAddress);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindByCustomer() {
        Integer customerId = 33;
        List<Address> listAddresses = repository.findByCustomer(new Customer(customerId));
        assertThat(listAddresses.size()).isGreaterThan(0);

        listAddresses.forEach(System.out::println);
    }

    @Test
    public void testFindByIdAndCustomer() {
        Integer addressId = 1;
        Integer customerId =33;

        Address address = repository.findByIdAndCustomer(addressId, customerId);

        assertThat(address).isNotNull();
        System.out.println(address);
    }

    @Test
    public void testUpdate() {
        Integer addressId = 1;
        String phoneNumber = "0808999999";

        Address address = repository.findById(addressId).get();
        address.setPhoneNumber(phoneNumber);

        Address updatedAddress = repository.save(address);
       assertThat(updatedAddress.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    public void testDeleteByIdAndCustomer() {
        Integer addressId = 3;
        Integer customerId = 33;

        repository.deleteByIdAndCustomer(addressId, customerId);

        Address address = repository.findByIdAndCustomer(addressId, customerId);
        assertThat(address).isNull();
    }

    @Test
    public void testSetDefault() {
        Integer addressId = 4;
        repository.setDefaultAddress(addressId);

        Address address = repository.findById(addressId).get();
        assertThat(address.isDefaultForShipping()).isTrue();
    }

    @Test
    public void testSetNonDefaultAddresses() {
        Integer addressId = 4;
        Integer customerId = 33;
        repository.setNonDefaultForOthers(addressId, customerId);
    }
    @Test
    public void testGetDefault() {
        Integer customerId = 33;
        Address address = repository.findDefaultByCustomer(customerId);
        assertThat(address).isNotNull();
        System.out.println(address);
    }
}
