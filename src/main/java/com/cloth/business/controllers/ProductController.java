package com.cloth.business.controllers;

import com.cloth.business.configurations.annotations.CheckRoles;
import com.cloth.business.entities.Product;
import com.cloth.business.services.ProductService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@CheckRoles({"ROLE_ADMIN", "ROLE_PRODUCT_CREATE"})
	@PostMapping("")
	public ResponseEntity<Product> createProduct(@Valid @ModelAttribute Product product) {				
		Product newProduct = productService.createProduct(product);
		return ResponseEntity.ok(newProduct);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		Product product = productService.getProductById(id);
		return ResponseEntity.ok(product);
	}

//	@GetMapping("")
//	public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
//		Page<Product> products = productService.getAllProducts(pageable);
//		return ResponseEntity.ok(products);
//	}
//	
//	
	@GetMapping("")
	public ResponseEntity<?> getAllProducts() {
		return ResponseEntity.ok(productService.getAllProductsList());
	}
	
	
	@GetMapping("/search")
	public ResponseEntity<?> searchProducts(
			@RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "size", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "desc", required = false) String sortDirection,
			@RequestParam(value = "query", defaultValue = "", required = false) String query 
			){
		return ResponseEntity.ok(productService.searchProduct(query, pageNumber, pageSize, sortBy, sortDirection));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(
			@PathVariable Long id,
			@RequestBody Product product) {
		Product updatedProduct = productService.updateProduct(id, product);
		return updatedProduct != null ? ResponseEntity.ok(updatedProduct)
				: ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}
