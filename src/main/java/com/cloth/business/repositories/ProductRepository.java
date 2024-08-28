package com.cloth.business.repositories;

import com.cloth.business.entities.Product;
import com.cloth.business.entities.ProductCategory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByCategoryAndNameAndSize(ProductCategory category, String name, String size);

//	@Query("SELECT p FROM Product p WHERE " +
//           "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
//           "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
//           "LOWER(p.size) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
//           "LOWER(p.remark) LIKE LOWER(CONCAT('%', :query, '%'))")
//	Page<Product> searchProducts(@Param("query") String query, Pageable pageable);
	
	
	
	
	
	@Query("SELECT p FROM Product p WHERE " +
		       "(LOWER(p.name) LIKE LOWER(CONCAT('%', :term1, '%')) OR " +
		       "LOWER(p.size) LIKE LOWER(CONCAT('%', :term1, '%')) OR " +
		       "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :term1, '%')) OR " +
		       "LOWER(p.remark) LIKE LOWER(CONCAT('%', :term1, '%'))) AND " +
		       "(LOWER(p.name) LIKE LOWER(CONCAT('%', :term2, '%')) OR " +
		       "LOWER(p.size) LIKE LOWER(CONCAT('%', :term2, '%')) OR " +
		       "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :term2, '%')) OR " +
		       "LOWER(p.remark) LIKE LOWER(CONCAT('%', :term2, '%'))) AND " +
		       "(LOWER(p.name) LIKE LOWER(CONCAT('%', :term3, '%')) OR " +
		       "LOWER(p.size) LIKE LOWER(CONCAT('%', :term3, '%')) OR " +
		       "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :term3, '%')) OR " +
		       "LOWER(p.remark) LIKE LOWER(CONCAT('%', :term3, '%'))) AND " +
		       "(LOWER(p.name) LIKE LOWER(CONCAT('%', :term4, '%')) OR " +
		       "LOWER(p.size) LIKE LOWER(CONCAT('%', :term4, '%')) OR " +
		       "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :term4, '%')) OR " +
		       "LOWER(p.remark) LIKE LOWER(CONCAT('%', :term4, '%')))")
		Page<Product> searchProducts(@Param("term1") String term1, 
		                             @Param("term2") String term2,
		                             @Param("term3") String term3,
		                             @Param("term4") String term4,
		                             Pageable pageable);

}
