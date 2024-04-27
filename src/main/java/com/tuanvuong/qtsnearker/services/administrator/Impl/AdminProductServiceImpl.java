package com.tuanvuong.qtsnearker.services.administrator.Impl;

import com.tuanvuong.qtsnearker.dao.administrator.AdminProductRepository;
import com.tuanvuong.qtsnearker.entity.Product;
import com.tuanvuong.qtsnearker.services.administrator.AdminProductService;
import com.tuanvuong.qtsnearker.services.exceptions.ProductNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AdminProductServiceImpl implements AdminProductService {
    @Autowired
    private AdminProductRepository adminProductRepository;

    @Override
    public List<Product> ListProductAll() {
        return adminProductRepository.findAll();
    }

    @Override
    public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);

        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
                return adminProductRepository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
            }

            return adminProductRepository.findAll(keyword, pageable);
        }

        if (categoryId != null && categoryId > 0) {
            String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
            return adminProductRepository.findAllInCategory(categoryId, categoryIdMatch, pageable);
        }

        return adminProductRepository.findAll(pageable);
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

        return adminProductRepository.save(product);
    }

    @Override
    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        adminProductRepository.updateEnabledStatus(id, enabled);
    }

    @Override
    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);
        Product productByName = adminProductRepository.findByName(name);

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
        Long countById = adminProductRepository.countById(id);

        if (countById == null || countById == 0) {
            throw new ProductNotFoundException("Không tìm thấy sản phẩm có id " + id);
        }

        adminProductRepository.deleteById(id);
    }

    @Override
    public Product get(Integer id) throws ProductNotFoundException {
        try {
            return adminProductRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ProductNotFoundException("Không tìm thấy sản phẩm có id " + id);
        }
    }

    @Override
    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
            adminProductRepository.updateEnabledStatus(id, enabled);
    }
    @Override
    public void saveProductPrice(Product productInForm) {
        Product productInDB = adminProductRepository.findById(productInForm.getId()).get();

        productInDB.setCost(productInForm.getCost());
        productInDB.setPrice(productInForm.getPrice());
        productInDB.setDiscountPercent(productInForm.getDiscountPercent());

        adminProductRepository.save(productInDB);
    }

}
