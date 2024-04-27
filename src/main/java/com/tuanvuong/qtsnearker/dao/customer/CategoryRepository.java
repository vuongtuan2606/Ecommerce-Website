package com.tuanvuong.qtsnearker.dao.customer;

import com.tuanvuong.qtsnearker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>, PagingAndSortingRepository<Category,Integer> {
    @Query("SELECT c FROM Category c WHERE c.enabled = true ORDER BY c.name ASC")
    public List<Category> findAllEnabled();

    @Query("SELECT c FROM Category c WHERE c.enabled = true AND c.alias = ?1")
    public Category findByAliasEnabled(String alias);
}
