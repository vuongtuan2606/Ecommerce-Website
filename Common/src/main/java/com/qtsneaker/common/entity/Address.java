package com.qtsneaker.common.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "tbl_addresses")
public class Address extends AbstractAddressWithProvince {

    @Column(name = "default_address")
    private boolean defaultForShipping;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

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
}

