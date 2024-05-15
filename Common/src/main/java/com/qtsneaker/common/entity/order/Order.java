package com.qtsneaker.common.entity.order;

import com.qtsneaker.common.entity.AbstractAddress;
import com.qtsneaker.common.entity.Address;
import com.qtsneaker.common.entity.Customer;
import jakarta.persistence.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "tbl_orders")

public class Order  extends AbstractAddress {
    @Column(nullable = false, length = 45)
    private String province;

    private Date orderTime;

    private float shippingCost;

    private float subtotal;

    private float total;


    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("updatedTime ASC")
    private List<OrderTrack> orderTracks = new ArrayList<>();

    public Order() {
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public float getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(float shippingCost) {
        this.shippingCost = shippingCost;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }


    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<OrderTrack> getOrderTracks() {
        return orderTracks;
    }

    public void setOrderTracks(List<OrderTrack> orderTracks) {
        this.orderTracks = orderTracks;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", subtotal=" + subtotal + ", paymentMethod=" + paymentMethod + ", status=" + status
                + ", customer=" + customer.getFullName() + "]";
    }

    public void copyAddressFromCustomer() {
        setFirstName(customer.getFirstName());
        setLastName(customer.getLastName());
        setPhoneNumber(customer.getPhoneNumber());
        setAddressLine1(customer.getAddressLine1());
        setDistrict(customer.getDistrict());
        setProvince(customer.getProvince().getName());
    }
    public void copyAddressFromOrder(Address address) {
        setFirstName(address.getFirstName());
        setLastName(address.getLastName());
        setPhoneNumber(address.getPhoneNumber());
        setAddressLine1(address.getAddressLine1());
        setDistrict(address.getDistrict());
        setProvince(address.getProvince().getName());
    }

    @Transient
    public String getDestination() {
        String destination = addressLine1 +",";
        if (district != null && !district.isEmpty()) destination += district + ", ";
        destination += province;

        return destination;
    }



    @Transient
    public boolean isCOD() {
        return paymentMethod.equals(PaymentMethod.COD);
    }

    @Transient
    public boolean isCancelled() {
        return hasStatus(OrderStatus.CANCELLED);
    }
    @Transient
    public boolean isProcessing() {
        return hasStatus(OrderStatus.PROCESSING);
    }

    @Transient
    public boolean isPackaged() {
        return hasStatus(OrderStatus.PACKAGED);
    }
    @Transient
    public boolean isShipping() {
        return hasStatus(OrderStatus.SHIPPING);
    }

    @Transient
    public boolean isDelivered() {
        return hasStatus(OrderStatus.DELIVERED);
    }


    public boolean hasStatus(OrderStatus status) {
        for (OrderTrack aTrack : orderTracks) {
            if (aTrack.getStatus().equals(status)) {
                return true;
            }
        }

        return false;
    }

    @Transient
    public String getRecipientName() {
        String name = firstName;
        if (lastName != null && !lastName.isEmpty()) name += " " + lastName;
        return name;
    }
    @Transient
    public String getRecipientAddress() {
        String address = addressLine1;

        if (!district.isEmpty()) address += ", " + district;

        if (province != null && !province.isEmpty()) address += ", " + province;

        return address;
    }
    @Transient
    public String getProductNames() {
        String productNames = "";

        productNames = "<ul>";

        for (OrderDetail detail : orderDetails) {
            productNames += "<li>" + detail.getProduct().getShortName() + "</li>";
        }

        productNames += "</ul>";

        return productNames;
    }

}
