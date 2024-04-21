package admin.brand;

import com.tuanvuong.qtsnearker.QtsnearkerApplication;
import com.tuanvuong.qtsnearker.dao.BrandRepository;
import com.tuanvuong.qtsnearker.entity.Brand;
import com.tuanvuong.qtsnearker.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@ContextConfiguration(classes = {QtsnearkerApplication.class})
@DataJpaTest(showSql = false)
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BrandRepositoryTests {

    @Autowired
    private BrandRepository brandRepository;


    @Test
    public void testCreateBrand() {
        Category Ultraboost = new Category(13);
        Brand Adidas  = new Brand("Adidas");
        Adidas.getCategories().add(Ultraboost);

        Brand savedBrand = brandRepository.save(Adidas);

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

        Brand savedBrand = brandRepository.save(Nike );

        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }


    @Test
    public void testFindAll() {
        Iterable<Brand> brands = brandRepository.findAll();
        brands.forEach(System.out::println);

        assertThat(brands).isNotEmpty();
    }

    @Test
    public void testGetById() {
        Brand brand = brandRepository.findById(2).get();

        assertThat(brand.getName()).isEqualTo("Nike");
    }

    @Test
    public void testUpdateName() {
        String newName = "Adidas2";
        Brand Adidas = brandRepository.findById(1).get();
        Adidas.setName(newName);

        Brand savedBrand = brandRepository.save(Adidas);
        assertThat(savedBrand.getName()).isEqualTo(newName);
    }

    @Test
    public void testDelete() {
        Integer id = 1;
        brandRepository.deleteById(id);

        Optional<Brand> result = brandRepository.findById(id);

        assertThat(result.isEmpty());
    }

}
