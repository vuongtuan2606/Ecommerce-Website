package com.qtsneaker.ShopFrontEnd.setting;

import com.qtsneaker.ShopFrontEnd.ShopBackEndApplication;

import com.qtsneaker.ShopFrontEnd.dao.AdminCurrencyRepository;
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
	private AdminCurrencyRepository currencyRepository;
	
	@Test
	public void testCreateCurrencies() {
		List<Currency> listCurrencies = Arrays.asList(
			new Currency("Vietnamese đồng ", "₫", "VND"),
			new Currency("United States Dollar", "$", "USD")
		);

		currencyRepository.saveAll(listCurrencies);

		Iterable<Currency> iterable = currencyRepository.findAll();

		assertThat(iterable).size().isEqualTo(2);
	}
	@Test
	public void testListAllOrderByNameAsc() {

		List<Currency> currencies = currencyRepository.findAllByOrderByNameAsc();

		currencies.forEach(System.out::println);

		assertThat(currencies.size()).isGreaterThan(0);
	}

}
