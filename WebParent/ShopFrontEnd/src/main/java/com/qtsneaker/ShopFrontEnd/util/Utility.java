package com.qtsneaker.ShopFrontEnd.util;



import com.qtsneaker.ShopFrontEnd.security.oauth.CustomerOAuth2User;
import com.qtsneaker.ShopFrontEnd.setting.EmailSettingBag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Properties;

public class Utility {
	// Phương thức để lấy URL của trang web
	public static String getSiteURL(HttpServletRequest request) {
		// Lấy URL của trang web từ yêu cầu
		String siteURL = request.getRequestURL().toString();

		// Loại bỏ đường dẫn của Servlet để chỉ lấy URL gốc
		return siteURL.replace(request.getServletPath(), "");
	}

	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		// Tạo đối tượng mail sender
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		// Thiết lập các cấu hình cho mail sender
		mailSender.setHost(settings.getHost());
		mailSender.setPort(settings.getPort());
		mailSender.setUsername(settings.getUsername());
		mailSender.setPassword(settings.getPassword());

		// Thiết lập các thuộc tính của mail sender
		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());
		mailSender.setJavaMailProperties(mailProperties);

		// Trả về đối tượng mail sender
		return mailSender;
	}


	 // Trả về địa chỉ email của khách hàng đã xác thực.
	public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
		// Lấy đối tượng principal từ request
		Object principal = request.getUserPrincipal();

		// Kiểm tra nếu không có principal, trả về null
		if (principal == null)
			return null;

		// Khởi tạo biến lưu trữ địa chỉ email của khách hàng
		String customerEmail = null;

		// Xác định loại principal và lấy địa chỉ email tương ứng
		if (principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
			// Nếu sử dụng username/password hoặc remember-me, lấy địa chỉ email từ principal

			customerEmail = request.getUserPrincipal().getName();

		} else if (principal instanceof OAuth2AuthenticationToken) {
			// Nếu sử dụng OAuth2, lấy địa chỉ email từ OAuth2User

			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;

			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();

			customerEmail = oauth2User.getEmail();
		}

		return customerEmail;
	}

}
