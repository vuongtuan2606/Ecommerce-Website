package com.tuanvuong.qtsnearker.services;

import com.tuanvuong.qtsnearker.entity.Product;
import com.tuanvuong.qtsnearker.services.exceptions.ProductNotFoundException;

import java.util.List;

public interface ProductService {
    List<Product> ListProductAll();

    Product save(Product product);
    void updateProductEnabledStatus(Integer id, boolean enabled);

    String checkUnique(Integer id, String name);
    void delete(Integer id) throws ProductNotFoundException;

    Product get(Integer id) throws ProductNotFoundException;

    void updateCategoryEnabledStatus(Integer id, boolean enabled);
}
