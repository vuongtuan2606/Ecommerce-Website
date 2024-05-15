package com.qtsneaker.ShopFrontEnd.dao;


import com.qtsneaker.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>, PagingAndSortingRepository<Product, Integer> {

	@Query("SELECT p FROM Product p WHERE p.enabled = true "
			+ "AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%)"
			+ " ORDER BY p.name ASC")
	public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);

	public Product findByAlias(String alias);

	@Query("SELECT p FROM Product p WHERE p.enabled = true ORDER BY p.name ASC")
	public Page<Product> findAllProductByEnabled(Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.enabled = true AND p.category.id = :categoryId AND p.alias != :alias ORDER BY p.name ASC")
	public List<Product> findTop4SimilarProducts(Integer categoryId,String alias);

	@Query(value = "SELECT * FROM tbl_products WHERE enabled = true AND "
			+ "MATCH(name, short_description, full_description) AGAINST (?1)",
			nativeQuery = true)
	public Page<Product> search(String keyword, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.enabled =true  ORDER BY p.createdTime DESC LIMIT 4")
	public List<Product> findTop4ProductNew();

	@Query("SELECT p FROM Product p WHERE p.enabled =true and p.discountPercent != 0")
	public Page<Product> listProductSaleOf(Pageable pageable);

}
