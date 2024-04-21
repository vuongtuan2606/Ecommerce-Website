package com.tuanvuong.qtsnearker.rest.administrator;


import com.tuanvuong.qtsnearker.dto.CategoryDTO;
import com.tuanvuong.qtsnearker.entity.Brand;
import com.tuanvuong.qtsnearker.entity.Category;
import com.tuanvuong.qtsnearker.services.BrandService;
import com.tuanvuong.qtsnearker.services.exceptions.BrandNotFoundException;
import com.tuanvuong.qtsnearker.services.exceptions.BrandNotFoundRestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RestController
public class BrandRestController {
    @Autowired
    private BrandService service;

    @PostMapping("/brand/check_unique")
    public String checkUnique(Integer id, String name) {
        return service.checkUnique(id, name);
    }

    @GetMapping("/brand/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundRestException {
        List<CategoryDTO> listCategories = new ArrayList<>();

        try {
            Brand brand = service.get(brandId);
            Set<Category> categories = brand.getCategories();

            for (Category category : categories) {
                CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
                listCategories.add(dto);
            }

            return listCategories;
        } catch (BrandNotFoundException e) {
            throw new BrandNotFoundRestException();
        }
    }
}
