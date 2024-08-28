package com.cloth.business.repositories;

import com.cloth.business.entities.Product;
import com.cloth.business.entities.ProductCategory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByCategoryAndNameAndSize(ProductCategory category, String name, String size);
}
