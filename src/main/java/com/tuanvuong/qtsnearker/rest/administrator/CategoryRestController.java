package com.tuanvuong.qtsnearker.rest.administrator;

import com.tuanvuong.qtsnearker.services.administrator.AdminCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

	@Autowired
	private AdminCategoryService adminCategoryService;
	
	@PostMapping("/categories/check_unique")
	public String checkUnique(@Param("id") Integer id,
							  @Param("name") String name,
							  @Param("alias") String alias) {

		return adminCategoryService.checkUnique(id, name, alias);
	}
}
