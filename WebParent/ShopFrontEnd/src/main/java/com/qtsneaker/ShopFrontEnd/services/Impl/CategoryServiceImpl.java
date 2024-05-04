package com.qtsneaker.ShopFrontEnd.services.Impl;


import com.qtsneaker.ShopFrontEnd.dao.CategoryRepository;
import com.qtsneaker.ShopFrontEnd.services.CategoryService;
import com.qtsneaker.common.entity.Category;

import com.qtsneaker.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Không lấy danh mục con
    @Override
    public List<Category> listNoChildrenCategories() {

        List<Category> listNoChildrenCategories = new ArrayList<>();

        List<Category> listEnabledCategories = categoryRepository.findAllEnabled();

        listEnabledCategories.forEach(category -> {

            Set<Category> children = category.getChildren();

            if(children == null || children.size() == 0){
                listNoChildrenCategories.add(category);
            }
        });

        return listNoChildrenCategories;
    }

    // tìm danh mục với alias truyền vào
    @Override
    public Category getCategory(String alias) throws CategoryNotFoundException {
        Category category = categoryRepository.findByAliasEnabled(alias);
        if (category == null) {
            throw new CategoryNotFoundException("Could not find any categories with alias " + alias);
        }

        return category;
    }

    // trả về một list
    // các danh mục cha của một danh mục con cùng với danh mục con

    public List<Category> getCategoryParents(Category children) {
        // Tạo một list mới để lưu trữ các danh mục cha.
        List<Category> listParents = new ArrayList<>();

        //Lấy danh mục cha của danh mục con được truyền vào.
        Category parent = children.getParent();

        while (parent != null) {
            //Thêm danh mục cha vào đầu danh sách
            listParents.add(0, parent);
            // Di chuyển lên một cấp để lấy danh mục cha của danh mục cha hiện tại.
            parent = parent.getParent();
        }

        //  Cuối cùng, sau khi đã thêm tất cả các danh mục cha vào danh sách,
        //  cũng thêm danh mục con ban đầu vào danh sách.
        //  Điều này đảm bảo rằng danh mục con cũng được bao gồm trong danh sách trả về.
        listParents.add(children);

        // Trả về danh sách các danh mục cha cùng với danh mục con
        return listParents;
    }
}
