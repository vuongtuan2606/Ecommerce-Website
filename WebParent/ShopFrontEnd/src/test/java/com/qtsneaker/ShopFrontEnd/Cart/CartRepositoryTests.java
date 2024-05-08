package com.qtsneaker.ShopFrontEnd.Cart;

import com.qtsneaker.ShopFrontEnd.ShopFrontEndApplication;
import com.qtsneaker.ShopFrontEnd.dao.CartRepository;
import com.qtsneaker.common.entity.Cart;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {ShopFrontEndApplication.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Rollback(false)
public class CartRepositoryTests {

    @Autowired private CartRepository cartRepository;

    @Autowired private TestEntityManager entityManager;

    /*Test lưu CartItem vào db*/
    @Test
    public void testSaveItem1() {
        Integer customerId = 1;
        Integer productId = 18;

        Customer customer = entityManager.find(Customer.class, customerId);
        Product product = entityManager.find(Product.class, productId);

        Cart newItem = new Cart();
        newItem.setCustomer(customer);
        newItem.setProduct(product);
        newItem.setQuantity(2);

        Cart savedItem = cartRepository.save(newItem);

        assertThat(savedItem.getId()).isGreaterThan(0);
    }

    /*Test lưu nhiều CartItem vào db*/
    @Test
    public void testSaveItems2() {
        Integer customerId = 1;
        Integer productId = 19;

        Customer customer = entityManager.find(Customer.class, customerId);
        Product product = entityManager.find(Product.class, productId);

        Cart item1 = new Cart();
        item1.setCustomer(customer);
        item1.setProduct(product);
        item1.setQuantity(1);

        Cart item2 = new Cart();
        item2.setCustomer(new Customer(customerId));
        item2.setProduct(new Product(20));
        item2.setQuantity(2);

        Iterable<Cart> iterable = cartRepository.saveAll(List.of(item1, item2));

        assertThat(iterable).size().isGreaterThan(0);
    }

    /*Test Lấy list CartItem của khách hàng ( điều kiện test đúng = 3 vì trong db có 3 items)*/
    @Test
    public void testFindByCustomer() {
        Integer customerId = 1;
        List<Cart> listItems = cartRepository.findByCustomer(new Customer(customerId));

        listItems.forEach(System.out::println);

        assertThat(listItems.size()).isEqualTo(3);
    }

    /* Test tìm theo CartItem theo customerId và productId  */
    @Test
    public void testFindByCustomerAndProduct() {
        Integer customerId = 1;
        Integer productId = 18;

        Cart item = cartRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        assertThat(item).isNotNull();

        System.out.println(item);
    }

    /* Test cập nhật số lượng mới*/
    @Test
    public void testUpdateQuantity() {
        Integer customerId = 1;
        Integer productId = 18;
        Integer quantity = 4;

        cartRepository.updateQuantity(quantity, customerId, productId);

        Cart item = cartRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        assertThat(item.getQuantity()).isEqualTo(4);
    }

    /* Test xóa CartItem theo customerId và productId   */
    @Test
    public void testDeleteByCustomerAndProduct() {
        Integer customerId = 1;
        Integer productId = 19;

        cartRepository.deleteByCustomerAndProduct(customerId, productId);

        Cart item = cartRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        assertThat(item).isNull();
    }
}
