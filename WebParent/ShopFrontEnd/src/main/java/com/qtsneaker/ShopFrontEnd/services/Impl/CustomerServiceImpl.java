package com.qtsneaker.ShopFrontEnd.services.Impl;


import com.qtsneaker.ShopFrontEnd.dao.CustomerRepository;
import com.qtsneaker.ShopFrontEnd.services.CustomerService;
import com.qtsneaker.common.entity.AuthenticationType;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.exception.CustomerNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import net.bytebuddy.utility.RandomString;
import java.util.Date;


@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepository.findByEmail(email);
		return customer == null;
	}


	@Override
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(AuthenticationType.DATABASE);

		String radomCode = RandomString.make(64);
		customer.setVerificationCode(radomCode);

		customerRepository.save(customer);
	}

	@Override
	public void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}

	@Override
	public boolean verify(String verificationCode) {
		Customer customer = customerRepository.findByVerificationCode(verificationCode);

		if (customer == null || customer.isEnabled()) {
			return false;
		} else {
			customerRepository.enable(customer.getId());
			return true;
		}
	}

	@Override
	public void updateAuthenticationType(Customer customer, AuthenticationType type) {

		// Kiểm tra xem loại xác thực mới có khác với loại xác thực hiện tại của khách hàng không
		if (!customer.getAuthenticationType().equals(type)) {

			// Nếu có khác, thực hiện cập nhật loại xác thực mới vào cơ sở dữ liệu
			customerRepository.updateAuthenticationType(customer.getId(), type);
		}
	}

	@Override
	public Customer getCustomerByEmail(String email) {
		return customerRepository.findByEmail(email);
	}

	@Override
	public void addNewCustomerUponOAuthLogin(String name, String email, AuthenticationType authenticationType) {
		Customer customer = new Customer();

		customer.setEmail(email);
		setName(name, customer);
		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(authenticationType);
		customer.setPassword("");
		customer.setAddressLine1("");
		customer.setPhoneNumber("");

		customerRepository.save(customer);
	}

	public void setName(String name, Customer customer) {
		// Tách tên thành mảng các từ
		String[] nameArray = name.split(" ");

		// Kiểm tra xem tên có chứa ít nhất hai phần hay không
		if (nameArray.length < 2) {
			// Nếu không, sử dụng toàn bộ tên làm tên đầu tiên và không có họ
			customer.setFirstName(name);
			customer.setLastName("");
		} else {
			// Nếu có, sử dụng phần đầu tiên làm tên đầu tiên
			String firstName = nameArray[0];
			customer.setFirstName(firstName);

			// Sử dụng phần còn lại làm họ
			String lastName = name.replaceFirst(firstName + " ", "");
			customer.setLastName(lastName);
		}
	}

	@Override
	public void update(Customer customerInForm) {
		Customer customerInDB = customerRepository.findById(customerInForm.getId()).get();

		// chỉ cập nhật Password khi khách hàng đăng nhập bằng tài khoản, mật khẩu
		if (customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {

			if (!customerInForm.getPassword().isEmpty()) {
				String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());

				customerInForm.setPassword(encodedPassword);

			} else {
				customerInForm.setPassword(customerInDB.getPassword());
			}

		} else {
			customerInForm.setPassword(customerInDB.getPassword());
		}

		customerInForm.setEnabled(customerInDB.isEnabled());
		customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		customerInForm.setVerificationCode(customerInDB.getVerificationCode());
		customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
		customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());

		customerRepository.save(customerInForm);
	}

	@Override
	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {

		Customer customer = customerRepository.findByEmail(email);

		// Kiểm tra xem khách hàng có tồn tại không
		if (customer != null) {
			// Tạo token ngẫu nhiên có độ dài 30 ký tự
			String token = RandomString.make(30);

			// Cập nhật ResetPasswordToken cho khách hàng
			customer.setResetPasswordToken(token);

			customerRepository.save(customer);

			// Trả về token đã tạo
			return token;
		} else {
			throw new CustomerNotFoundException("Không tìm thấy khách hàng có email: " + email);
		}
	}


	@Override
	public Customer getByResetPasswordToken(String token) {
		// tìm khách hàng dựa trên ResetPasswordToken
		return customerRepository.findByResetPasswordToken(token);
	}

	@Override
	public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {

		// Tìm kiếm khách hàng dựa trên ResetPasswordToken
		Customer customer = customerRepository.findByResetPasswordToken(token);

		if (customer == null) {
			throw new CustomerNotFoundException("Không tìm thấy khách hàng: token không hợp lệ");
		}
		// Cập nhật mật khẩu mới
		customer.setPassword(newPassword);

		// Đặt lại ResetPasswordToken về null
		customer.setResetPasswordToken(null);

		// Mã hóa mật khẩu
		encodePassword(customer);

		customerRepository.save(customer);
	}
}
