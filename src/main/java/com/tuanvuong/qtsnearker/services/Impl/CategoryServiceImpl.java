package com.tuanvuong.qtsnearker.services.Impl;

import com.tuanvuong.qtsnearker.dto.CategoryPageInfo;
import com.tuanvuong.qtsnearker.dao.CategoryRepository;
import com.tuanvuong.qtsnearker.entity.Category;
import com.tuanvuong.qtsnearker.services.CategoryService;
import com.tuanvuong.qtsnearker.services.exceptions.CategoryNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> listByPage(CategoryPageInfo pageInfo ,int pageNum, String sortDir , String keyword){

        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sort);

        Page<Category> pageCategories = null;

        if (keyword != null && !keyword.isEmpty()) {
            pageCategories = categoryRepository.search(keyword, pageable);
        } else {
            pageCategories = categoryRepository.findRootCategories(pageable);
        }

        List<Category> rootCategories = pageCategories.getContent();

        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());

        if (keyword != null && !keyword.isEmpty()) {

            List<Category> searchResult = pageCategories.getContent();

            for (Category category : searchResult) {
                category.setHasChildren(category.getChildren().size() > 0);
            }
            return searchResult;

        } else {
            return listHierarchicalCategories(rootCategories, sortDir);
        }
    }

    // danh sách Danh mục được sử dụng trong form
    @Override
    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = categoryRepository.findRootCategories(Sort.by("name").ascending());

        for (Category category : categoriesInDB) {
            categoriesUsedInForm.add(Category.copyIdAndName(category));

            Set<Category> children = sortSubCategories(category.getChildren());

            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

                listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
            }
        }

        return categoriesUsedInForm;
    }

    // danh sách Danh mục phụ được sử dụng trong form
    @Override
    public void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm,Category parent, int subLevel) {

        int newSubLevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren());

        for (Category subCategory : children) {

            String name = "";

            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }

            name += subCategory.getName();

            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

            listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
        }
    }
    // liệt kê Danh mục phân cấp
    @Override
    public List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));

                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
            }
        }

        return hierarchicalCategories;
    }

    // liệt kê các danh mục phân cấp phụ
    @Override
    public void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
                                              Category parent, int subLevel, String sortDir) {
        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
        int newSubLevel = subLevel + 1;

        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();

            hierarchicalCategories.add(Category.copyFull(subCategory, name));

            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
        }

    }

    @Override
    public SortedSet<Category> sortSubCategories(Set<Category> children) {

        return sortSubCategories(children, "asc");
    }

    @Override
    public SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category cat1, Category cat2) {
                if (sortDir.equals("asc")) {
                    return cat1.getName().compareTo(cat2.getName());
                } else {
                    return cat2.getName().compareTo(cat1.getName());
                }
            }
        });

        sortedChildren.addAll(children);

        return sortedChildren;
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

    @Override
    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {

        categoryRepository.updateEnabledStatus(id, enabled);
    }

    @Override
    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countById(id);

        if (countById == null || countById == 0) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }

        categoryRepository.deleteById(id);
    }

    @Override
    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);

        // tìm category name trong db
        Category categoryByName = categoryRepository.findByName(name);

        if (isCreatingNew) {
            // nếu tồn tại category name
            if (categoryByName != null) {
                return "DuplicateName";
            }
            else {
                // tìm category alias trong db
                Category categoryByAlias = categoryRepository.findByAlias(alias);

                // nếu tồn tại category alias
                if (categoryByAlias != null) {
                    return "DuplicateAlias";
                }
            }
        }
        else {
            if (categoryByName != null && categoryByName.getId() != id) {
                return "DuplicateName";
            }

            Category categoryByAlias = categoryRepository.findByAlias(alias);
            if (categoryByAlias != null && categoryByAlias.getId() != id) {
                return "DuplicateAlias";
            }

        }

        return "OK";
    }

}