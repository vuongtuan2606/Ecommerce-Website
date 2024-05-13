package com.qtsneaker.ShopBackEnd.services.Brand;


import com.qtsneaker.ShopBackEnd.dao.AdminBrandRepository;
import com.qtsneaker.ShopBackEnd.exception.BrandNotFoundException;
import com.qtsneaker.common.entity.Brand;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AdminBrandServiceImpl implements AdminBrandService {

    @Autowired
    private AdminBrandRepository adminBrandRepository;

    @Override
    public List<Brand> listAll() {
        return adminBrandRepository.findAll();
    }

    @Override
    public Page<Brand> listByPage( int pageNum, String sortDir, String keyword) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE, sort);

        if(keyword != null){
            return adminBrandRepository.findAll(keyword, pageable);
        }

        return  adminBrandRepository.findAll(pageable);
    }

    @Override
    public Brand save(Brand brand) {
        return adminBrandRepository.save(brand);
    }

    @Override
    public Brand get(Integer id) throws BrandNotFoundException {
        try {
            return adminBrandRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }
    }

    @Override
    public void delete(Integer id) throws BrandNotFoundException {
        Long countById = adminBrandRepository.countById(id);

        if (countById == null || countById == 0) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }

        adminBrandRepository.deleteById(id);
    }

    @Override
    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);

        Brand brandByName = adminBrandRepository.findByName(name);

        if (isCreatingNew) {
            if (brandByName != null) {
                return "Duplicate";
            }
        }
        else {
            if (brandByName != null && brandByName.getId() != id) {
                return "Duplicate";
            }
        }
        return "OK";
    }
}
