package com.qtsneaker.ShopFrontEnd.security;

import com.qtsneaker.ShopFrontEnd.services.Customer.CustomerService;
import com.qtsneaker.common.entity.AuthenticationType;
import com.qtsneaker.common.entity.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired private CustomerService customerService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
										HttpServletResponse response,
										Authentication authentication)
			throws ServletException, IOException {

		// Lấy thông tin người dùng từ Authentication
		CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();

		// Lấy thông tin khách hàng từ UserDetails
		Customer customer = userDetails.getCustomer();

		// Cập nhật loại xác thực của khách hàng trong cơ sở dữ liệu thành DATABASE
		customerService.updateAuthenticationType(customer, AuthenticationType.DATABASE);

		// Gọi phương thức onAuthenticationSuccess() của lớp cha để hoàn thành xử lý xác thực thành công
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	
}
