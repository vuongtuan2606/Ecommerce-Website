package com.qtsneaker.ShopFrontEnd.setting;


import com.qtsneaker.ShopFrontEnd.setting.EmailSettingBag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.javamail.JavaMailSenderImpl;

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
}
