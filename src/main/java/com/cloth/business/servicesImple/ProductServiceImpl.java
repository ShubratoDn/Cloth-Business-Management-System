package com.cloth.business.servicesImple;

import com.cloth.business.entities.Product;
import com.cloth.business.entities.ProductCategory;
import com.cloth.business.exceptions.ResourceAlreadyExistsException;
import com.cloth.business.exceptions.ResourceNotFoundException;
import com.cloth.business.repositories.ProductCategoryRepository;
import com.cloth.business.repositories.ProductRepository;
import com.cloth.business.services.FileServices;
import com.cloth.business.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

	
	
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductCategoryRepository productCategoryRepository; 
    
    @Autowired
    private FileServices fileServices;

    @Override
    public Product createProduct(Product product) {
    	
    	//setting product category
    	ProductCategory productCategory = productCategoryRepository.findByName(product.getProductCategory());
    	if(productCategory == null) {
    		ProductCategory newCategory = new ProductCategory();
    		newCategory.setName(product.getProductCategory());
    		ProductCategory save = productCategoryRepository.save(newCategory);
    		product.setCategory(save);
    	}else {
    		product.setCategory(productCategory);
    	}
    	
    	
    	List<Product> products = productRepository.findByCategoryAndNameAndSize(product.getCategory(), product.getName(), product.getSize());
    	if(products != null && products.size() > 0) {
    		throw new ResourceAlreadyExistsException("The product is already exists");
    	}
    	
    	String uploadProductImage = fileServices.uploadProductImage(product.getProductImage());
    	product.setImage(uploadProductImage);
    	
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product", id+""));
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            Product existingProduct = product.get();
            existingProduct.setCategory(productDetails.getCategory());
            existingProduct.setName(productDetails.getName());
            existingProduct.setSize(productDetails.getSize());
            existingProduct.setRemark(productDetails.getRemark());
            return productRepository.save(existingProduct);
        } else {
            return null;
        }
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
