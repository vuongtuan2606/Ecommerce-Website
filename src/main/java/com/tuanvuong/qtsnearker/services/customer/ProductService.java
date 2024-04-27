package com.tuanvuong.qtsnearker.services.customer;

import com.tuanvuong.qtsnearker.entity.Product;
import com.tuanvuong.qtsnearker.services.exceptions.ProductNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public  static final int PRODUCT_PER_PAGE = 4;
    public  static final int SEARCH_RESULTS_PER_PAGE = 4;
    public Page<Product> listByCategory(int pageNum, Integer categoryId);
    public Product getProduct(String alias) throws ProductNotFoundException;
    public Page<Product> listAllProduct(int pageNum);

    Page<Product> search(String keyword, int pageNum);
}
