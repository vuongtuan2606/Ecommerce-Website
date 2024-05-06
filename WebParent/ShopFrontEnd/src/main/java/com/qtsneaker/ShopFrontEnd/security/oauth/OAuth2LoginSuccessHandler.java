package com.qtsneaker.ShopFrontEnd.security.oauth;

import com.qtsneaker.ShopFrontEnd.services.CustomerService;
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
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired private CustomerService customerService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
										HttpServletResponse response,
										Authentication authentication
	) throws ServletException, IOException {

		// Ép kiểu principal trong authentication thành CustomerOAuth2User
		CustomerOAuth2User oauth2User = (CustomerOAuth2User) authentication.getPrincipal();

		// Lấy thông tin tên từ đối tượng oauth2User
		String name = oauth2User.getName();
		String email = oauth2User.getEmail();

		System.out.println("OAuth2LoginSuccessHandler:" + name +" | " + email  );

		// Lấy thông tin khách hàng từ dịch vụ khách hàng bằng địa chỉ email
		Customer customer = customerService.getCustomerByEmail(email);

		// Kiểm tra xem khách hàng có tồn tại hay không
		if (customer == null) {

			// Nếu không tồn tại, thêm khách hàng mới vào cơ sở dữ liệu
			customerService.addNewCustomerUponOAuthLogin(name, email );
		} else {

			// Nếu tồn tại, cập nhật thông tin và loại xác thực của khách hàng
			customerService.updateAuthenticationType(customer, AuthenticationType.GOOGLE);
		}

		// Gọi phương thức onAuthenticationSuccess() của class cha để xử lý thành công xác thực
		super.onAuthenticationSuccess(request, response, authentication);
	}


}
