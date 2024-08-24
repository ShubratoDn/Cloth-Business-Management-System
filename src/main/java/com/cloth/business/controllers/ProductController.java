package com.cloth.business.controllers;

import com.cloth.business.entities.Product;
import com.cloth.business.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		Product newProduct = productService.createProduct(product);
		return ResponseEntity.ok(newProduct);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		Product product = productService.getProductById(id);
		return ResponseEntity.ok(product);
	}

	@GetMapping("")
	public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
		Page<Product> products = productService.getAllProducts(pageable);
		return ResponseEntity.ok(products);
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
