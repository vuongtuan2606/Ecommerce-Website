package com.tuanvuong.qtsnearker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class QtsnearkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(QtsnearkerApplication.class, args);
	}

}
