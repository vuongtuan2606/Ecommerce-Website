package com.tuanvuong.qtsnearker.services.administrator;


import com.tuanvuong.qtsnearker.entity.Brand;
import com.tuanvuong.qtsnearker.services.exceptions.BrandNotFoundException;
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
