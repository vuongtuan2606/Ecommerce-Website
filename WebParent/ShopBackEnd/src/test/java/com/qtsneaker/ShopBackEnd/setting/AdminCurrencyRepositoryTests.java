package com.qtsneaker.ShopBackEnd.setting;

import com.qtsneaker.ShopBackEnd.ShopBackEndApplication;

import com.qtsneaker.ShopBackEnd.dao.AdminCurrencyRepository;
import com.qtsneaker.common.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ContextConfiguration(classes = {ShopBackEndApplication.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AdminCurrencyRepositoryTests {

	@Autowired
	private AdminCurrencyRepository adminCurrencyRepository;
	
	@Test
	public void testCreateCurrencies() {
		List<Currency> listCurrencies = Arrays.asList(
			new Currency("Vietnamese đồng ", "₫", "VND"),
			new Currency("United States Dollar", "$", "USD")
		);

		adminCurrencyRepository.saveAll(listCurrencies);

		Iterable<Currency> iterable = adminCurrencyRepository.findAll();

		assertThat(iterable).size().isEqualTo(2);
	}
	@Test
	public void testListAllOrderByNameAsc() {

		List<Currency> currencies = adminCurrencyRepository.findAllByOrderByNameAsc();

		currencies.forEach(System.out::println);

		assertThat(currencies.size()).isGreaterThan(0);
	}

}
