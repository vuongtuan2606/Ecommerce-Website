package com.qtsneaker.ShopFrontEnd.services.Product;


import com.qtsneaker.ShopFrontEnd.dao.ProductRepository;
import com.qtsneaker.common.entity.Product;
import com.qtsneaker.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductsServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> listByCategory(int pageNum, Integer categoryId) {
        String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
        Pageable pageable = PageRequest.of(pageNum -1,PRODUCT_PER_PAGE);

        return productRepository.listByCategory(categoryId,categoryIdMatch,pageable);
    }
    @Override
    public Product getProduct(String alias) throws ProductNotFoundException {
        Product product = productRepository.findByAlias(alias);
        if (product == null) {
            throw new ProductNotFoundException("Could not find any product with alias " + alias);
        }

        return product;
    }


    // get  all product by enabled true for shop
    @Override
    public Page<Product> listAllProduct(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum -1,PRODUCT_PER_PAGE);
        return productRepository.findAllProductByEnabled(pageable);
    }

    @Override
    public Page<Product> listProductSaleOf(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum -1,PRODUCT_PER_PAGE);
        return productRepository.listProductSaleOf(pageable);
    }
    @Override
    public Page<Product> search(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
        return productRepository.search(keyword, pageable);

    }

    @Override
    public List<Product> findTop4SimilarProducts(Integer categoryId, String alias) {
        return productRepository.findTop4SimilarProducts(categoryId,alias);
    }

    @Override
    public List<Product> productNewHomePage() {
        return productRepository.findTop4ProductNew();
    }




}
