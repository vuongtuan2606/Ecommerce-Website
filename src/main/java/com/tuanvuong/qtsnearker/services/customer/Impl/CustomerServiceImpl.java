package com.tuanvuong.qtsnearker.services.customer.Impl;

import com.tuanvuong.qtsnearker.dao.customer.CustomerRepository;
import com.tuanvuong.qtsnearker.entity.Customer;
import com.tuanvuong.qtsnearker.services.customer.CustomerService;
import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
