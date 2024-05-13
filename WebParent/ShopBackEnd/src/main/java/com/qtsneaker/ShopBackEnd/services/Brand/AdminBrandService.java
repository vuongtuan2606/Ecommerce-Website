package com.qtsneaker.ShopBackEnd.services.Brand;


import com.qtsneaker.ShopBackEnd.exception.BrandNotFoundException;
import com.qtsneaker.common.entity.Brand;

import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminBrandService {
    int BRAND_PER_PAGE = 5;
    List<Brand> listAll();

    Page<Brand> listByPage(int pageNum, String sortDir, String keyword );
    Brand save(Brand brand);

    Brand get(Integer id) throws BrandNotFoundException;

    void delete(Integer id) throws BrandNotFoundException;

    String checkUnique(Integer id, String name);

}
