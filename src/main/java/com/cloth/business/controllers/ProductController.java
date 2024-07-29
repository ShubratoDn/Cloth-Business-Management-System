package com.cloth.business.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/products")
@RestController
public class ProductController {

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<?> getAllProducts(){
		return ResponseEntity.ok("All Product List");
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRODUCT_GET')")
	@GetMapping("/{productId}")
	public ResponseEntity<?> getProduct(@PathVariable Long productId){
		return ResponseEntity.ok("Product with id " + productId);
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRODUCT_CREATE')")
	@PostMapping("/")
	public ResponseEntity<?> addProduct(){
		return ResponseEntity.ok("Product has been created!");
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRODUCT_UPDATE')")
	@PutMapping("/")
	public ResponseEntity<?> updateProduct(){
		return ResponseEntity.ok("Product Updated!");
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRODUCT_DELETE')")
	@DeleteMapping("/")
	public ResponseEntity<?> deleteProduct(){
		return ResponseEntity.ok("Product Deleted!");
	}
	
}
