package com.qtsneaker.ShopBackEnd.category;


import com.qtsneaker.ShopBackEnd.dao.AdminCategoryRepository;
import com.qtsneaker.ShopBackEnd.services.Impl.AdminCategoryServiceImpl;
import com.qtsneaker.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


/* @ExtendWith(MockitoExtension.class):
*   Mark that this test class uses Mockito to create and manage mock objects.
*
* @ExtendWith(SpringExtension.class):
*   Marks that this test class uses the Spring Testing Framework to manage the lifecycle of Spring beans and inject dependencies
*
* @MockBean:
*   Create a mock bean for CategoryRepository. This allows to control the behavior of CategoryRepository in test
*
* @InjectMocks:
* 	Injects an object of CategoryServiceImpl and automatically injects fake beans into it.
* 	This ensures that when calling the method on categoryService,
* 	CategoryServiceImpl methods are called with fake beans.
* */

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class AdminAdminCategoryServiceTests {

	@MockBean
	private AdminCategoryRepository repo;
	
	@InjectMocks
	private AdminCategoryServiceImpl categoryService;
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicateName() {
		Integer id = null;
		String name = "Converse";
		String alias = "abc";
		
		Category category = new Category(id, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(category);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicateAlias() {
		Integer id = null;
		String name = "nameTest";
		String alias = "converse";
		
		Category category = new Category(id, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateAlias");
	}	
	
	
	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		Integer id = null;
		String name = "nameTest";
		String alias = "converse";
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("OK");
	}		

	@Test
	public void testCheckUniqueInEditModeReturnDuplicateName() {
		Integer id = 1;
		String name = "Converse";
		String alias = "abc";
		
		Category category = new Category(2, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(category);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicateAlias() {
		Integer id = 1;
		String name = "nameTest";
		String alias = "converse";
		
		Category category = new Category(2, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		Integer id = 1;
		String name = "nameTest";
		String alias = "converse";
		
		Category category = new Category(id, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("OK");
	}		
}
