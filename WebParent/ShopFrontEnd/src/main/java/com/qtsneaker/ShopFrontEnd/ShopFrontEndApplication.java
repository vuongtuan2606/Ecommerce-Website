package com.qtsneaker.ShopFrontEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.qtsneaker.common.entity"})
public class ShopFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopFrontEndApplication.class, args);
	}

}
