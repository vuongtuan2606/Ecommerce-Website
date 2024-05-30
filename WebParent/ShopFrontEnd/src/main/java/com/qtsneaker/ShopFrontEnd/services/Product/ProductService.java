package com.qtsneaker.ShopFrontEnd.services.Product;


import com.qtsneaker.common.entity.Product;
import com.qtsneaker.common.entity.Size;
import com.qtsneaker.common.exception.ProductNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    static final int PRODUCT_PER_PAGE = 8;
    static final int SEARCH_RESULTS_PER_PAGE = 8;
    Page<Product> listByCategory(int pageNum, Integer categoryId,String sortField, String sortDir);
    Product getProduct(String alias) throws ProductNotFoundException;
    Page<Product> listAllProduct(int pageNum, String sortField, String sortDir);
    Page<Product> search(String keyword, int pageNum);
    List<Product> findTop4SimilarProducts(Integer categoryId, String alias);
    List<Product> productNewHomePage();
    Page<Product> listProductSaleOf(int pageNum, String sortField, String sortDir);

    void decreaseProductQuantity(Integer id, Integer quantityOrdered);


}
