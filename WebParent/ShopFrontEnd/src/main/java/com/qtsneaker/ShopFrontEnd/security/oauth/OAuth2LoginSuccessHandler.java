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
		String  clientName = oauth2User.getClientName();

		System.out.println("OAuth2LoginSuccessHandler:" + name +" | " + email  );
		System.out.println("CLient name :" + clientName);

		// Lấy loại xác thực từ tên của client
		AuthenticationType authenticationType = getAuthenticationType(clientName);

		// Lấy thông tin khách hàng từ dịch vụ khách hàng bằng địa chỉ email
		Customer customer = customerService.getCustomerByEmail(email);

		// Kiểm tra xem khách hàng có tồn tại hay không
		if (customer == null) {

			// Nếu không tồn tại, thêm khách hàng mới vào cơ sở dữ liệu
			customerService.addNewCustomerUponOAuthLogin(name, email ,authenticationType);
		} else {
			// cập nhật lại tên khách hàng
			oauth2User.setFullName(customer.getFullName());

			// Nếu tồn tại, cập nhật thông tin và loại xác thực của khách hàng
			customerService.updateAuthenticationType(customer, authenticationType);

		}

		// Gọi phương thức onAuthenticationSuccess() của class cha để xử lý thành công xác thực
		super.onAuthenticationSuccess(request, response, authentication);
	}

	/**
	 * Xác định và trả về loại xác thực dựa trên tên của khách hàng.
	 * Nếu tên khách hàng là "Google", phương thức trả về AuthenticationType.GOOGLE.
	 * Nếu tên khách hàng là "Facebook", phương thức trả về AuthenticationType.FACEBOOK.
	 * Nếu không, phương thức trả về AuthenticationType.DATABASE.
	 *
	 * @param clientName Tên của khách hàng
	 * @return Loại xác thực dựa trên tên của khách hàng
	 */
	private AuthenticationType getAuthenticationType(String clientName) {
		if (clientName.equals("Google")) {
			return AuthenticationType.GOOGLE;
		} else if (clientName.equals("Facebook")) {
			return AuthenticationType.FACEBOOK;
		} else {
			return AuthenticationType.DATABASE;
		}
	}



}
