package com.qtsneaker.common.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractAddressWithProvince extends AbstractAddress {
    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    @Override
    public String toString() {
        String address = firstName;

        if (lastName != null && !lastName.isEmpty()) address += " " + lastName;

        if (!phoneNumber.isEmpty()) address += ", Số điện thoại: " + phoneNumber;

        if (!addressLine1.isEmpty()) address += ", " + addressLine1;

        if (district != null && !district.isEmpty()) address += ", " + district;

        address += ", " + province.getName();


        return address;
    }
}
