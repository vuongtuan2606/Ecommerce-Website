package com.tuanvuong.qtsnearker.services.Impl;

import com.tuanvuong.qtsnearker.dao.ProductRepository;
import com.tuanvuong.qtsnearker.entity.Product;
import com.tuanvuong.qtsnearker.services.ProductService;
import com.tuanvuong.qtsnearker.services.exceptions.ProductNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> ListProductAll() {
        return productRepository.findAll();
    }

    @Override
    public Product save(Product product) {
        // nếu đang thêm mới tạo CreatedTime bằng thời gian hiện tại
        if(product.getId() == null){
            product.setCreatedTime(new Date());

        }

        // nếu Alias rỗng thay thế alias mặc định bằng tên sản phẩm
        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }

        product.setUpdatedTime(new Date());

        return productRepository.save(product);
    }

    @Override
    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        productRepository.updateEnabledStatus(id, enabled);
    }

    @Override
    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);
        Product productByName = productRepository.findByName(name);

        if (isCreatingNew) {

            if (productByName != null) return "Duplicate";
        }
        else {

            if (productByName != null && productByName.getId() != id) {
                return "Duplicate";
            }

        }

        return "OK";
    }

    @Override
    public void delete(Integer id) throws ProductNotFoundException {
        Long countById = productRepository.countById(id);

        if (countById == null || countById == 0) {
            throw new ProductNotFoundException("Không tìm thấy sản phẩm có id " + id);
        }

        productRepository.deleteById(id);
    }

    @Override
    public Product get(Integer id) throws ProductNotFoundException {
        try {
            return productRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ProductNotFoundException("Không tìm thấy sản phẩm có id " + id);
        }
    }

    @Override
    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
            productRepository.updateEnabledStatus(id, enabled);
    }
}
