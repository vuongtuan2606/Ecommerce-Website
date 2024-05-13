package com.qtsneaker.ShopFrontEnd.services.Brand;


import com.qtsneaker.ShopFrontEnd.dao.BrandsRepositoty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BrandsServiceImpl implements BrandsService {

    @Autowired
    private BrandsRepositoty brandsRepositoty;



}
