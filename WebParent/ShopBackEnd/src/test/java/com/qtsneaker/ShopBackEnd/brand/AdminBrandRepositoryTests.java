package com.qtsneaker.ShopBackEnd.brand;


import com.qtsneaker.ShopBackEnd.ShopBackEndApplication;
import com.qtsneaker.ShopBackEnd.dao.AdminBrandRepository;
import com.qtsneaker.common.entity.Brand;
import com.qtsneaker.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@ContextConfiguration(classes = {ShopBackEndApplication.class})
@DataJpaTest(showSql = false)
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminBrandRepositoryTests {

    @Autowired
    private AdminBrandRepository adminBrandRepository;


    @Test
    public void testCreateBrand() {
        Category Ultraboost = new Category(13);
        Brand Adidas  = new Brand("Adidas");
        Adidas.getCategories().add(Ultraboost);

        Brand savedBrand = adminBrandRepository.save(Adidas);

        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }


    @Test
    public void testCreateBrand2() {
        Category Air_Force_1 = new Category(16);
        Category Jordan = new Category(14);

        Brand Nike  = new Brand("Nike");
        Nike.getCategories().add(Air_Force_1);
        Nike.getCategories().add(Jordan);

        Brand savedBrand = adminBrandRepository.save(Nike );

        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }


    @Test
    public void testFindAll() {
        Iterable<Brand> brands = adminBrandRepository.findAll();
        brands.forEach(System.out::println);

        assertThat(brands).isNotEmpty();
    }

    @Test
    public void testGetById() {
        Brand brand = adminBrandRepository.findById(2).get();

        assertThat(brand.getName()).isEqualTo("Nike");
    }

    @Test
    public void testUpdateName() {
        String newName = "Adidas2";
        Brand Adidas = adminBrandRepository.findById(1).get();
        Adidas.setName(newName);

        Brand savedBrand = adminBrandRepository.save(Adidas);
        assertThat(savedBrand.getName()).isEqualTo(newName);
    }

    @Test
    public void testDelete() {
        Integer id = 1;
        adminBrandRepository.deleteById(id);

        Optional<Brand> result = adminBrandRepository.findById(id);

        assertThat(result.isEmpty());
    }

}
