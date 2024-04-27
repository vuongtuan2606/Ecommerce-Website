package com.tuanvuong.qtsnearker.services.customer;

import com.tuanvuong.qtsnearker.entity.Category;
import com.tuanvuong.qtsnearker.services.exceptions.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {
    List<Category> listNoChildrenCategories();

    Category getCategory(String alias) throws CategoryNotFoundException;

    List<Category> getCategoryParents(Category child);
}
