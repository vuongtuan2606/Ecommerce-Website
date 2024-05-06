package com.qtsneaker.ShopFrontEnd.category;


import com.qtsneaker.ShopFrontEnd.ShopFrontEndApplication;
import com.qtsneaker.ShopFrontEnd.dao.CategoryRepository;
import com.qtsneaker.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {ShopFrontEndApplication.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryRepositoryTests {

	@Autowired private CategoryRepository repo;

	// hiển thị Category có enabled = true
	@Test
	public void testListEnabledCategories() {
		List<Category> categories = repo.findAllEnabled();
		categories.forEach(category -> {
			System.out.println(category.getName() + " (" + category.isEnabled() + ")");
		});
	}

	// tìm theo alias
	@Test
	public void testFindCategoryByAlias() {
		String alias = "ultraboost";
		Category category = repo.findByAliasEnabled(alias);
		System.out.println("alias:"+category);
		assertThat(category).isNotNull();
	}
}
