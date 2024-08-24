package com.cloth.business.servicesImple;

import com.cloth.business.entities.ProductCategory;
import com.cloth.business.repositories.ProductCategoryRepository;
import com.cloth.business.services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategory createProductCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public Optional<ProductCategory> getProductCategoryById(Long id) {
        return productCategoryRepository.findById(id);
    }

    @Override
    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepository.findAll();
    }

    @Override
    public ProductCategory updateProductCategory(Long id, ProductCategory productCategoryDetails) {
        Optional<ProductCategory> category = productCategoryRepository.findById(id);

        if (category.isPresent()) {
            ProductCategory existingCategory = category.get();
            existingCategory.setName(productCategoryDetails.getName());
            return productCategoryRepository.save(existingCategory);
        } else {
            return null;
        }
    }

    @Override
    public void deleteProductCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }
}
