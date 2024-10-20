package com.cloth.business.services;

import com.cloth.business.entities.Product;
import com.cloth.business.payloads.PageResponse;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface ProductService {
    Product createProduct(Product product);
    Product getProductById(Long id);
    Page<Product> getAllProducts(Pageable pageable);
    List<Product> getAllProductsList();
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    

	PageResponse searchProduct(String query, int page, int size, String sortBy, String sortDirection);
}
