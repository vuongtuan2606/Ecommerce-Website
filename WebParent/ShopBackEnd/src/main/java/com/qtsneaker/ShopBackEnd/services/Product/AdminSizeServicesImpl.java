package com.qtsneaker.ShopBackEnd.services.Product;


import com.qtsneaker.ShopBackEnd.dao.AdminSizeRepository;
import com.qtsneaker.common.entity.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminSizeServicesImpl implements AdminSizeServices {
    @Autowired private AdminSizeRepository sizeRepository;
    @Override
    public List<Size> getListAll() {
        List<Size> allSizes = sizeRepository.findAll();
        List<Size> sortedSizes = allSizes.stream()
                .sorted(Comparator.comparingInt(Size::getId))
                .collect(Collectors.toList());
        return sortedSizes;
    }
}
