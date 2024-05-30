package com.qtsneaker.ShopFrontEnd.services.Product;


import com.qtsneaker.ShopFrontEnd.dao.ProductRepository;
import com.qtsneaker.ShopFrontEnd.dao.ProductSizeRepository;
import com.qtsneaker.common.entity.Product;
import com.qtsneaker.common.entity.Size;
import com.qtsneaker.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductsServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @Override
    public Page<Product> listByCategory(int pageNum, Integer categoryId,String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
        Pageable pageable = PageRequest.of(pageNum -1,PRODUCT_PER_PAGE,sort);

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
    public Page<Product> listAllProduct(int pageNum, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum -1,PRODUCT_PER_PAGE,sort);
        return productRepository.findAllProductByEnabled(pageable);
    }

    @Override
    public Page<Product> listProductSaleOf(int pageNum, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum -1,PRODUCT_PER_PAGE,sort);
        return productRepository.listProductSaleOf(pageable);
    }

    @Override
    public void decreaseProductQuantity(Integer id, Integer quantityOrdered) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            Integer currentQuantity = product.getProductQuantity();
            if (currentQuantity >= quantityOrdered) {
                // Giảm số lượng sản phẩm trong kho đi quantity
                product.setProductQuantity(currentQuantity - quantityOrdered);
                productRepository.save(product);
            } else {
                // Xử lý khi số lượng yêu cầu vượt quá số lượng hiện có trong kho
                throw new IllegalArgumentException("Số lượng yêu cầu vượt quá số lượng hiện có trong kho.");
            }
        } else {
            // Xử lý khi không tìm thấy sản phẩm
            throw new IllegalArgumentException("Không tìm thấy sản phẩm với ID đã cho.");
        }
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
