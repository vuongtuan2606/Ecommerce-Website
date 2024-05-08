package com.qtsneaker.ShopFrontEnd.controller;


import com.qtsneaker.ShopFrontEnd.services.CustomerService;
import com.qtsneaker.ShopFrontEnd.util.Utility;
import com.qtsneaker.common.entity.Customer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class ControllerHelper {
	@Autowired private CustomerService customerService;

	/* Lấy thông tin của khách hàng đã xác thực từ HttpServletRequest.*/
	public Customer getAuthenticatedCustomer(HttpServletRequest request) {
		// Lấy địa chỉ email của khách hàng đã xác thực bằng  Utility
		String email = Utility.getEmailOfAuthenticatedCustomer(request);

		// Lấy thông tin của khách hàng dựa trên email
		return customerService.getCustomerByEmail(email);
	}
}
