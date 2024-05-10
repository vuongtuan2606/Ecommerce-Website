package com.qtsneaker.ShopBackEnd.setting;



import com.qtsneaker.ShopBackEnd.ShopBackEndApplication;
import com.qtsneaker.ShopBackEnd.dao.AdminSettingRepository;
import com.qtsneaker.common.entity.setting.Setting;
import com.qtsneaker.common.entity.setting.SettingCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ContextConfiguration(classes = {ShopBackEndApplication.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AdminSettingRepositoryTests {

	@Autowired
	private AdminSettingRepository repo;
	
	@Test
	public void testCreateGeneralSettings() {
		Setting siteName = new Setting("SITE_NAME", "Qt Sneaker", SettingCategory.GENERAL);
		Setting siteLogo = new Setting("SITE_LOGO", "Qt_Sneaker.png", SettingCategory.GENERAL);
		Setting copyright = new Setting("COPYRIGHT", "Copyright (C) 2024 Shopme Ltd.", SettingCategory.GENERAL);
		
		repo.saveAll(List.of(siteName, siteLogo, copyright));
		
		Iterable<Setting> iterable = repo.findAll();
		
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testCreateCurrencySettings() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);
		
		repo.saveAll(List.of(currencyId, symbol, symbolPosition, decimalPointType, 
				decimalDigits, thousandsPointType));
		
	}

	@Test
	public void testCreateMailSettings() {
		Setting mailHot = new Setting("MAIL_HOST", "mstp.gmail.com",SettingCategory.MAIL_SERVER);
		Setting mailPort = new Setting("MAIL_PORT", "123",SettingCategory.MAIL_SERVER);
		Setting mailUserName = new Setting("MAIL_USERNAME", "username",SettingCategory.MAIL_SERVER);
		Setting mailPassWord = new Setting("MAIL_PASSWORD", "password",SettingCategory.MAIL_SERVER);
		Setting smtp_auth = new Setting("SMTP_AUTH", "true",SettingCategory.MAIL_SERVER);
		Setting smtp_secured = new Setting("SMTP_SECURED", "true",SettingCategory.MAIL_SERVER);
		Setting mailForm = new Setting("MAIL_FROM", "qtsneaker@gmail.com",SettingCategory.MAIL_SERVER);
		Setting mailSenderName = new Setting("MAIL_SENDER_NAME", "qt sneaker company",SettingCategory.MAIL_SERVER);
		Setting customerVerifySubject = new Setting("CUSTOMER_VERIFY_SUBJECT", "Email subject",SettingCategory.MAIL_TEMPLATES);
		Setting customerVerifyContent = new Setting("CUSTOMER_VERIFY_CONTENT", "Email content",SettingCategory.MAIL_TEMPLATES);

		repo.saveAll(List.of(mailHot,mailPort,mailUserName,mailPassWord,
				mailForm,smtp_auth,smtp_secured,mailSenderName,customerVerifySubject,customerVerifyContent));
	}


	@Test
	public void testListSettingsByCategory() {
		List<Setting> settings = repo.findByCategory(SettingCategory.GENERAL);

		settings.forEach(System.out::println);
	}
	

}
