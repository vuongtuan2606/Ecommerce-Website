package com.qtsneaker.ShopFrontEnd.customer;



import com.qtsneaker.ShopFrontEnd.ShopFrontEndApplication;
import com.qtsneaker.ShopFrontEnd.dao.CustomerRepository;
import com.qtsneaker.common.entity.AuthenticationType;
import com.qtsneaker.common.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {ShopFrontEndApplication.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

	@Autowired private CustomerRepository customerRepository;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testCreateCustomer1() {
		Customer customer = new Customer();
		customer.setFirstName("nguyen");
		customer.setLastName("van a");
		customer.setPassword("nguyenvana");
		customer.setEmail("nguyenvana@gmail.com");
		customer.setPhoneNumber("0899275363");
		customer.setCreatedTime(new Date());
		Customer savedCustomer = customerRepository.save(customer);
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateCustomer2() {
		Customer customer = new Customer();
		customer.setFirstName("nguyen");
		customer.setLastName("van b");
		customer.setPassword("nguyenvanb");
		customer.setEmail("nguyenvanb@gmail.com");
		customer.setPhoneNumber("0899275362");
		customer.setCreatedTime(new Date());
		Customer savedCustomer = customerRepository.save(customer);
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}

	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = customerRepository.findAll();
		customers.forEach(System.out::println);

		assertThat(customers).hasSizeGreaterThan(1);
	}

	@Test
	public void testUpdateCustomer() {
		Integer customerId = 1;
		String lastName = "van hai";

		Customer customer = customerRepository.findById(customerId).get();
		customer.setLastName(lastName);
		customer.setEnabled(true);

		Customer updatedCustomer = customerRepository.save(customer);
		assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
	}

	@Test
	public void testGetCustomer() {
		Integer customerId = 2;
		Optional<Customer> findById = customerRepository.findById(customerId);

		assertThat(findById).isPresent();

		Customer customer = findById.get();
		System.out.println(customer);
	}

	@Test
	public void testDeleteCustomer() {
		Integer customerId = 2;
		customerRepository.deleteById(customerId);

		Optional<Customer> findById = customerRepository.findById(customerId);
		assertThat(findById).isNotPresent();
	}

	@Test
	public void testFindByEmail() {
		String email = "nguyenvana@gmail.com";
		Customer customer = customerRepository.findByEmail(email);

		assertThat(customer).isNotNull();
		System.out.println(customer);
	}


	@Test
	public void testEnableCustomer() {
		Integer customerId = 1;
		customerRepository.enable(customerId);

		Customer customer = customerRepository.findById(customerId).get();
		assertThat(customer.isEnabled()).isTrue();
	}

	@Test
	public void testUpdateAuthenticationType() {
		Integer id = 27;
		customerRepository.updateAuthenticationType(id, AuthenticationType.DATABASE);

		Customer customer = customerRepository.findById(id).get();

		assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.DATABASE);
	}

}
