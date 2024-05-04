package com.qtsneaker.ShopFrontEnd.services.Impl;


import com.qtsneaker.ShopFrontEnd.dao.BrandsRepositoty;
import com.qtsneaker.ShopFrontEnd.services.BrandsService;
import com.qtsneaker.common.entity.Brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandsServiceImpl implements BrandsService {

    @Autowired
    private BrandsRepositoty brandsRepositoty;



}
