package com.qtsneaker.ShopFrontEnd.services.Impl;

import com.qtsneaker.ShopFrontEnd.dao.CartRepository;
import com.qtsneaker.ShopFrontEnd.dao.ProductRepository;
import com.qtsneaker.ShopFrontEnd.exception.CartException;
import com.qtsneaker.ShopFrontEnd.services.CartService;
import com.qtsneaker.common.entity.Cart;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired private CartRepository cartRepository;

    @Autowired private ProductRepository productRepository;

    /* Thêm sản phẩm vào giỏ hàng*/
    @Override
    public Integer addProducts(Integer productId, Integer quantity, Customer customer) throws CartException {
        // Khởi tạo  updatedQuantity với giá trị ban đầu là quantity
        Integer updatedQuantity = quantity;

        // Tạo đối tượng Product từ productId
        Product product = new Product(productId);

        // Tìm kiếm cartItem tương ứng trong cơ sở dữ liệu dựa trên customer và product
        Cart cartItem = cartRepository.findByCustomerAndProduct(customer, product);

        // Nếu mục cartItem đã tồn tại
        if (cartItem != null) {

            // Cập nhật số lượng mới bằng tổng của số lượng hiện tại và số lượng được thêm vào
            updatedQuantity = cartItem.getQuantity() + quantity;

            // Kiểm tra nếu tổng số lượng mới vượt quá giới hạn cho phép
            if (updatedQuantity > 5) {
                // Ném ra một ngoại lệ CartException với thông báo phù hợp
                throw new CartException("Giỏ hàng của bạn đã đạt đến số lượng tối đa " + cartItem.getQuantity() + " mặt hàng,"
                                       + " Số lượng tối đa cho phép là 5.");
            }
        } else {

            // Nếu cartItem chưa tồn tại, tạo cartItem mới
            cartItem = new Cart();

            // Thiết lập customer và product cho cartItem
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
        }

        // Thiết lập số lượng được cập nhật cho cartItem
        cartItem.setQuantity(updatedQuantity);

        // Lưu vào cơ sở dữ liệu
        cartRepository.save(cartItem);

        // Trả về số lượng được cập nhật
        return updatedQuantity;
    }

    /* Lấy list Cart của khách hàng*/
    @Override
    public List<Cart> listCartItems(Customer customer) {
        return cartRepository.findByCustomer(customer);
    }

   /* Cập nhật số lượng của một sản phẩm trong giỏ hàng*/
    @Override
    public float updateQuantity(Integer productId, Integer quantity, Customer customer) {

        // Cập nhật số lượng của sản phẩm trong giỏ hàng
        cartRepository.updateQuantity(quantity, customer.getId(), productId);

        // Lấy thông tin sản phẩm từ productRepository dựa trên productId
        Product product = productRepository.findById(productId).get();

        // Tính tổng cộng giá trị của sản phẩm sau khi cập nhật số lượng
        float subtotal = product.getDiscountPrice() * quantity;

        // Trả về tổng cộng giá trị đã được cập nhật
        return subtotal;
    }

    /* Xóa một sản phẩm khỏi giỏ hàng*/
    @Override
    public void removeProduct(Integer productId, Customer customer) {
        cartRepository.deleteByCustomerAndProduct(customer.getId(),productId);
    }

   /* Xóa tất cả các sản phẩm trong giỏ hàng*/
    @Override
    public void deleteByCustomer(Customer customer) {
        cartRepository.deleteByCustomer(customer.getId());
    }
}
