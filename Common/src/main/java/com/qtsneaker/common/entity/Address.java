package com.qtsneaker.common.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_addresses")
public class Address extends IdBasedEntity {

	@Column(name = "first_name", nullable = false, length = 45)
	protected String firstName;

	@Column(name = "last_name", nullable = false, length = 45)
	protected String lastName;

	@Column(name = "phone_number", nullable = false, length = 15)
	protected String phoneNumber;

	@Column(name = "address_line_1", nullable = false, length = 64)
	protected String addressLine1;

	@Column(name = "default_address")
	private boolean defaultForShipping;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "province_id")
	private Province province;


    @Column(name = "district",nullable = false, length = 64)
    protected String district;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }


    public boolean isDefaultForShipping() {
        return defaultForShipping;
    }

    public void setDefaultForShipping(boolean defaultForShipping) {
        this.defaultForShipping = defaultForShipping;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        String address = firstName;

        if (lastName != null && !lastName.isEmpty()) address += " " + lastName;

        if (!addressLine1.isEmpty()) address += ", " + addressLine1;

        if (district != null && !district.isEmpty()) address += ", " + district;

        address += ", " + province.getName();

        if (!phoneNumber.isEmpty()) address += ", Phone Number: " + phoneNumber;

        return address;
    }
}
