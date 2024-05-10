package com.qtsneaker.ShopBackEnd.services;


import com.qtsneaker.ShopBackEnd.dto.CategoryPageInfo;
import com.qtsneaker.common.entity.Category;

import com.qtsneaker.common.exception.CategoryNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public interface AdminCategoryService {
    int ROOT_CATEGORIES_PER_PAGE = 5;


    List<Category> listByPage(CategoryPageInfo pageInfo , int pageNum, String sortDir, String keyword );

    List<Category> listCategoriesUsedInForm();

    void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel);

    List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir);

    void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel, String sortDir);

    SortedSet<Category> sortSubCategories(Set<Category> children);

    SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir);

    Category save(Category category);

    Category get(Integer id) throws CategoryNotFoundException;

    void updateCategoryEnabledStatus(Integer id, boolean enabled);

    void delete(Integer id) throws CategoryNotFoundException;

    String checkUnique(Integer id, String name, String alias);
}
