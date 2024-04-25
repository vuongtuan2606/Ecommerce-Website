package com.tuanvuong.qtsnearker.services;

import com.tuanvuong.qtsnearker.entity.Product;
import com.tuanvuong.qtsnearker.services.exceptions.ProductNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public static final int PRODUCTS_PER_PAGE = 3;

    List<Product> ListProductAll();
    Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId);
    Product save(Product product);
    void updateProductEnabledStatus(Integer id, boolean enabled);

    String checkUnique(Integer id, String name);
    void delete(Integer id) throws ProductNotFoundException;

    Product get(Integer id) throws ProductNotFoundException;

    void updateCategoryEnabledStatus(Integer id, boolean enabled);

    void saveProductPrice(Product productInForm);
}
