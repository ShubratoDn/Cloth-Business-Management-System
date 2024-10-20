package com.cloth.business.servicesImple;

import com.cloth.business.entities.Product;
import com.cloth.business.entities.ProductCategory;
import com.cloth.business.exceptions.ResourceAlreadyExistsException;
import com.cloth.business.exceptions.ResourceNotFoundException;
import com.cloth.business.helpers.HelperUtils;
import com.cloth.business.payloads.PageResponse;
import com.cloth.business.repositories.ProductCategoryRepository;
import com.cloth.business.repositories.ProductRepository;
import com.cloth.business.services.FileServices;
import com.cloth.business.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    	
    	if(product.getProductImage() != null){
			String uploadProductImage = fileServices.uploadProductImage(product.getProductImage());
			product.setImage(uploadProductImage);
		}
    	
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
    public List<Product> getAllProductsList() {
    	 return productRepository.findAll();
    }

    @Override
    public PageResponse searchProduct(String query, int page, int size, String sortBy, String sortDirection) {
    	Sort sort = null;
		if (sortDirection.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}
		
		Page<Product> pageInfo;
		
		 String[] terms = query.split("\\s+"); // Split query by spaces
		
		try {
			Pageable pageable = PageRequest.of(page, size, sort);
//			pageInfo = productRepository.searchProducts(query, pageable);
						
			// Call the repository method with as many terms as you can support
		    if (terms.length == 1) {
		        pageInfo = productRepository.searchProducts(terms[0], "", "", "", pageable);
		    } else if (terms.length == 2) {
		        pageInfo = productRepository.searchProducts(terms[0], terms[1], "", "", pageable);
		    } else if (terms.length == 3) {
		        pageInfo = productRepository.searchProducts(terms[0], terms[1], terms[2], "", pageable);
		    } else if (terms.length >= 4) {
		        pageInfo = productRepository.searchProducts(terms[0], terms[1], terms[2], terms[3], pageable);
		    } else {
		        // In case of no terms (unlikely since there's a search query)
		        pageInfo = Page.empty(pageable);
		    }
			
		} catch (Exception e) {
			Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
//			pageInfo = productRepository.searchProducts(query, pageable);
			
			// Call the repository method with as many terms as you can support
		    if (terms.length == 1) {
		        pageInfo = productRepository.searchProducts(terms[0], "", "", "", pageable);
		    } else if (terms.length == 2) {
		        pageInfo = productRepository.searchProducts(terms[0], terms[1], "", "", pageable);
		    } else if (terms.length == 3) {
		        pageInfo = productRepository.searchProducts(terms[0], terms[1], terms[2], "", pageable);
		    } else if (terms.length >= 4) {
		        pageInfo = productRepository.searchProducts(terms[0], terms[1], terms[2], terms[3], pageable);
		    } else {
		        // In case of no terms (unlikely since there's a search query)
		        pageInfo = Page.empty(pageable);
		    }
		}

		
		PageResponse pageToPageResponse = HelperUtils.pageToPageResponse(pageInfo);
		
    	return pageToPageResponse;
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
