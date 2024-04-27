package com.tuanvuong.qtsnearker.services.customer.Impl;

import com.tuanvuong.qtsnearker.dao.customer.BrandsRepositoty;
import com.tuanvuong.qtsnearker.entity.Brand;
import com.tuanvuong.qtsnearker.services.customer.BrandsService;
import com.tuanvuong.qtsnearker.services.exceptions.BrandNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandsServiceImpl implements BrandsService {

    @Autowired
    private BrandsRepositoty brandsRepositoty;



}
