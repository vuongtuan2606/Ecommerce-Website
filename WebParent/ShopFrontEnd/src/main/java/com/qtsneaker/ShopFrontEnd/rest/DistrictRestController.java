package com.qtsneaker.ShopFrontEnd.rest;


import com.qtsneaker.ShopFrontEnd.dao.DistrictRepository;
import com.qtsneaker.common.entity.District;
import com.qtsneaker.common.entity.DistrictDTO;
import com.qtsneaker.common.entity.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DistrictRestController {

	@Autowired private DistrictRepository districtRepository;
	
	@GetMapping("/list_district_by_province/{id}")
	public List<DistrictDTO> listByProvince(@PathVariable("id") Integer provinceId) {
		List<District> listDistrict = districtRepository.findByProvinceOrderByNameAsc(new Province(provinceId));
		List<DistrictDTO> result = new ArrayList<>();
		
		for (District district : listDistrict) {
			result.add(new DistrictDTO(district.getId(), district.getName()));
		}
		
		return result;
	}

}
