package com.qtsneaker.ShopFrontEnd.brand;


import com.qtsneaker.ShopFrontEnd.dao.AdminBrandRepository;
import com.qtsneaker.ShopFrontEnd.services.Impl.AdminBrandServiceImpl;
import com.qtsneaker.common.entity.Brand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class AdminAdminBrandServiceTests {
	
	@MockBean
	private AdminBrandRepository repo;
	
	@InjectMocks
	private AdminBrandServiceImpl service;
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicate() {
		Integer id = null;
		String name = "Nike";
		Brand brand = new Brand(name);
		
		Mockito.when(repo.findByName(name)).thenReturn(brand);
		
		String result = service.checkUnique(id, name);
		assertThat(result).isEqualTo("Duplicate");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		Integer id = null;
		String name = "sneaker";
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		
		String result = service.checkUnique(id, name);
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicate() {
		Integer id = 1;
		String name = "Nike";
		Brand brand = new Brand(id, name);
		
		Mockito.when(repo.findByName(name)).thenReturn(brand);

		String result = service.checkUnique(2, "Nike");
		assertThat(result).isEqualTo("Duplicate");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		Integer id = 1;
		String name = "Nike";
		Brand brand = new Brand(id, name);
		
		Mockito.when(repo.findByName(name)).thenReturn(brand);
		
		String result = service.checkUnique(id, "Pumaa");
		assertThat(result).isEqualTo("OK");
	}	
}
