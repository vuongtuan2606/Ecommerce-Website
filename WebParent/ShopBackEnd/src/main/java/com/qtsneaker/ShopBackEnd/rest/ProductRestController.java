package com.qtsneaker.ShopBackEnd.rest;



import com.qtsneaker.ShopBackEnd.services.Product.AdminProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

	@Autowired private AdminProductService adminProductService;
	
	@PostMapping("/products/check_unique")
	public String checkUnique(Integer id, String name) {
		return adminProductService.checkUnique(id, name);
	}	
	

}
