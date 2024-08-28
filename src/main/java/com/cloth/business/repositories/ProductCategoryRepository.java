package com.cloth.business.repositories;

import com.cloth.business.entities.ProductCategory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
	List<ProductCategory> findByNameContaining(String name);
	ProductCategory findByName(String name);
}
