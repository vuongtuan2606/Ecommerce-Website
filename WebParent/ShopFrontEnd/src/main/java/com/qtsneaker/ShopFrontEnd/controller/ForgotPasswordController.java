package com.qtsneaker.ShopFrontEnd.controller;


import com.qtsneaker.ShopFrontEnd.services.CustomerService;
import com.qtsneaker.ShopFrontEnd.services.SettingService;
import com.qtsneaker.ShopFrontEnd.setting.EmailSettingBag;
import com.qtsneaker.ShopFrontEnd.util.Utility;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.exception.CustomerNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

	@Autowired private CustomerService customerService;

	@Autowired private SettingService settingService;
	
	@GetMapping("/forgot_password")
	public String showRequestForm() {
		return "customer/forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model) {
		// Lấy địa chỉ email từ request
		String email = request.getParameter("email");

		try {
			// Cập nhật token cho khách hàng có email được cung cấp
			String token = customerService.updateResetPasswordToken(email);

			//Gửi email chứa đường link này đến email của khách hàng
			String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
			sendEmail(link, email);
			
			model.addAttribute("message", "Chúng tôi đã gửi liên kết đặt lại mật khẩu đến email của bạn. Vui lòng kiểm tra email! ");
		}
		catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		}
		catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Không thể gửi email");
		}
		
		return "customer/forgot_password_form";
	}
	
	private void sendEmail(String link, String email) throws UnsupportedEncodingException, MessagingException {

		EmailSettingBag emailSettings = settingService.getEmailSettings();

		// JavaMailSender
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

		// Địa chỉ email đến và tiêu đề của email
		String toAddress = email;
		String subject = "Đây là liên kết để đặt lại mật khẩu của bạn";

		// Nội dung của email, bao gồm đường link để đặt lại mật khẩu
		String content =  "<p> Xin chào,</p>"
						+ "<p> Bạn đã yêu cầu đặt lại mật khẩu của mình. </p>"
						+ " Click vào liên kết bên dưới để thay đổi mật khẩu của bạn: </p>"
						+ "<p> <a href=\"" + link + "\"> Thay đổi mật khẩu </a> </p>"
						+ "<br>"
						+ "<p> Bỏ qua email này nếu bạn nhớ mật khẩu của mình, "
						+ " hoặc bạn chưa thực hiện yêu cầu. </p>";

		// Tạo một MimeMessage và MimeMessageHelper để chuẩn bị email
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		// Đặt thông tin người gửi, người nhận và tiêu đề của email
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);

		// Đặt nội dung của email, với HTML được cho phép
		helper.setText(content, true);

		// gửi mail
		mailSender.send(message);
	}
	
	@GetMapping("/reset_password")
	public String showResetForm(String token, Model model) {
		// Lấy thông tin khách hàng dựa trên ResetPasswordToken
		Customer customer = customerService.getByResetPasswordToken(token);

		// Kiểm tra xem khách hàng có tồn tại không
		if (customer != null) {
			// thêm token vào model để sử dụng trong form đặt lại mật khẩu
			model.addAttribute("token", token);
		}
		else {
			// Nếu không tìm thấy khách hàng dựa trên token, hiển thị trang thông báo lỗi
			model.addAttribute("pageTitle", "Token không hợp lệ");
			model.addAttribute("message", "Token không hợp lệ");

			return "message";
		}
		
		return "customer/reset_password_form";
	}
	
	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		try {
			customerService.updatePassword(token, password);
			
			model.addAttribute("pageTitle", "Đặt lại mật khẩu ");
			model.addAttribute("title", "Đặt lại mật khẩu ");
			model.addAttribute("message", "Mật khẩu của bạn đã được thay đổi.");
			
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle", "Token không hợp lệ");
			model.addAttribute("message", e.getMessage());
		}	

		return "message";		
	}
}
