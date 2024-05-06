package com.qtsneaker.ShopFrontEnd.category;

import com.qtsneaker.ShopFrontEnd.ShopBackEndApplication;

import com.qtsneaker.ShopFrontEnd.dao.AdminCategoryRepository;
import com.qtsneaker.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {ShopBackEndApplication.class})
@DataJpaTest(showSql = false)
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminCategoryRepositoryTests {

    @Autowired
    private AdminCategoryRepository adminCategoryRepository;

    // Test creating top-level (root) categories
    @Test
    public void testCreateRootCategory() {
        Category category = new Category("Nike");

        Category save = adminCategoryRepository.save(category);

        assertThat(save.getId()).isGreaterThan(0);
    }

    // Test creating sub caregories
    @Test
    public void testCreateSubCategory() {
        Category parent = new Category(16);

        Category subCategory = new Category("Af1 ", parent);

        Category save= adminCategoryRepository.save(subCategory);

        assertThat(save.getId()).isGreaterThan(0);
    }

    // Test getting a category and its children
    @Test
    public void testGetCategory() {
        Category category = adminCategoryRepository.findById(2).get();

        System.out.println("category name : " +category.getName());

        Set<Category> children = category.getChildren();

        for (Category subCategory : children) {
            System.out.println("subCategory" + subCategory.getName());
        }

        assertThat(children.size()).isGreaterThan(0);
    }

    // test pring a category in hierarchical form
    @Test
    public void testPrintHierarchicalCategories() {
        Iterable<Category> categories = adminCategoryRepository.findAll();

        for (Category category : categories) {

            if (category.getParent() == null) {
                System.out.println(category.getName());

                Set<Category> children = category.getChildren();

                for (Category subCategory : children) {
                    System.out.println("--" + subCategory.getName());

                    printChildren(subCategory, 1);
                }
            }
        }
    }


    private  void printChildren(Category parent, int subLevel) {

        int newSubLevel = subLevel + 1;

        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {

            for (int i = 0; i < newSubLevel; i++) {
                System.out.print("--");
            }

            System.out.println(subCategory.getName());

            printChildren(subCategory, newSubLevel);
        }
    }

    @Test
    public void testListRootCategories() {
        List<Category> rootCategories = adminCategoryRepository.findRootCategories(Sort.by("name").ascending());
        rootCategories.forEach(cate -> System.out.println(cate.getName()));
    }

    // Test FindByName
    @Test
    public void testFindByName() {
        String name = "nike";
        Category category = adminCategoryRepository.findByName(name);

        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo(name);
    }

    // Test FindByAlias
    @Test
    public void testFindByAlias() {
        String alias = "adidas";
        Category category = adminCategoryRepository.findByAlias(alias);

        assertThat(category).isNotNull();
        assertThat(category.getAlias()).isEqualTo(alias);
    }



}
