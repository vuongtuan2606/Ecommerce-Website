package com.tuanvuong.qtsnearker.rest.administrator;


import com.tuanvuong.qtsnearker.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

	@Autowired private ProductService productService;
	
	@PostMapping("/products/check_unique")
	public String checkUnique(Integer id, String name) {
		return productService.checkUnique(id, name);
	}	
	

}
