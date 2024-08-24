package com.cloth.business.services;

import com.cloth.business.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    Product createProduct(Product product);
    Product getProductById(Long id);
    Page<Product> getAllProducts(Pageable pageable);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
}
