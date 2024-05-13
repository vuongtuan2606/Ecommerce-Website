package com.qtsneaker.ShopFrontEnd.services.Cart;

import com.qtsneaker.ShopFrontEnd.exception.CartException;
import com.qtsneaker.common.entity.Cart;
import com.qtsneaker.common.entity.Customer;

import java.util.List;

public interface CartService {

    /* Thêm sản phẩm vào giỏ hàng*/
    Integer addProducts(Integer productId, Integer quantity, Customer customer) throws CartException;

    /* List các mục trong giỏ hàng*/
    List<Cart> listCartItems(Customer customer);

    /* Cập nhật số lượng của một sản phẩm trong giỏ hàng*/
    float updateQuantity(Integer productId, Integer quantity, Customer customer);

    /* Xóa một sản phẩm khỏi giỏ hàng*/
    void removeProduct(Integer productId, Customer customer);

    /* Xóa tất cả các mục trong giỏ hàng*/
    void deleteByCustomer(Customer customer);

}
