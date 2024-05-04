package com.qtsneaker.ShopFrontEnd.services;

import com.qtsneaker.common.entity.Category;
import com.qtsneaker.common.exception.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {
    List<Category> listNoChildrenCategories();

    Category getCategory(String alias) throws CategoryNotFoundException;

    List<Category> getCategoryParents(Category child);
}
