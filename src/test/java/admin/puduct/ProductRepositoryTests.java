package admin.puduct;

import com.tuanvuong.qtsnearker.QtsnearkerApplication;
import com.tuanvuong.qtsnearker.dao.ProductRepository;
import com.tuanvuong.qtsnearker.entity.Brand;
import com.tuanvuong.qtsnearker.entity.Category;
import com.tuanvuong.qtsnearker.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@ContextConfiguration(classes = {QtsnearkerApplication.class})
@DataJpaTest(showSql = false)
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    /* Iterable<>
    *   là một interface được sử dụng để biểu diễn một tập hợp các phần tử có thể được lặp qua.
    *
    * Optional<>
    *    là một lớp được sử dụng để giải quyết vấn đề về giá trị null.
    *    Nó đại diện cho một giá trị có thể tồn tại hoặc không tồn tại.
    * */
    @Test
    public void testCreateProduct() {
        Brand brand = entityManager.find(Brand.class, 2);
        Category category = entityManager.find(Category.class, 16);

        Product product = new Product();
        product.setName("Nike Air Force 1 Shadow");
        product.setAlias("nike Air Force 1 Shadow");
        product.setShortDescription("Short description for  Air Force 1");
        product.setFullDescription("Full description for  Air Force 1");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(200);
        product.setCost(150);
        product.setEnabled(true);
        product.setInStock(true);

        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct = productRepository.save(product);

        // kiểm tra savedProduct có null không
        assertThat(savedProduct).isNotNull();
        // kiểm tra xem id có > 0 hay không
        assertThat(savedProduct.getId()).isGreaterThan(0);

    }
    @Test
    public void testListAllProducts() {
        Iterable<Product> iterableProducts = productRepository.findAll();

        iterableProducts.forEach(System.out::println);
    }

    @Test
    public void testFindProduct() {
        Integer id = 1;
        Product product = productRepository.findById(id).get();
        System.out.println(product);

        assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct() {
        Integer id = 1;
        Product product = productRepository.findById(id).get();
        product.setPrice(250);

        productRepository.save(product);

        Product updatedProduct = entityManager.find(Product.class, id);

        // Kiểm tra xem giá đã được cập nhật thành 250 chưa.
        // Nếu đúng, test sẽ thành công
        assertThat(updatedProduct.getPrice()).isEqualTo(250);
    }

    @Test
    public void testDeleteProduct() {
        Integer id = 3;
        productRepository.deleteById(id);

        Optional<Product> result = productRepository.findById(id);

        // Kiểm tra xem sản phẩm có id là 3 có tồn tại không.
        // Nếu không tồn tại  (nghĩa là result không chứa giá trị),
        // test sẽ thành công
        assertThat(!result.isPresent());
    }
}
