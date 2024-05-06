package com.qtsneaker.ShopFrontEnd.services.Impl;


import com.qtsneaker.ShopFrontEnd.dao.CustomerRepository;
import com.qtsneaker.ShopFrontEnd.services.CustomerService;
import com.qtsneaker.common.entity.AuthenticationType;
import com.qtsneaker.common.entity.Customer;
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
	public void addNewCustomerUponOAuthLogin(String name, String email) {
		Customer customer = new Customer();

		customer.setEmail(email);
		setName(name, customer);
		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(AuthenticationType.GOOGLE);
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
}
