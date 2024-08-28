package com.cloth.business.services;

import com.cloth.business.entities.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryService {
    ProductCategory createProductCategory(ProductCategory productCategory);
    Optional<ProductCategory> getProductCategoryById(Long id);
    List<ProductCategory> getAllProductCategories();
    List<ProductCategory> searchProductCategories(String query);
    ProductCategory updateProductCategory(Long id, ProductCategory productCategory);
    void deleteProductCategory(Long id);
}
