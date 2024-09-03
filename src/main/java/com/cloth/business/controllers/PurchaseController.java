package com.cloth.business.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.business.configurations.annotations.CheckRoles;
import com.cloth.business.entities.Purchase;

import jakarta.validation.Valid;

@RestController("/api/v1/purchases")
public class PurchaseController {

	@CheckRoles({"ROLE_ADMIN", "ROLE_PURCHASE_CREATE"})
	@PostMapping
	public ResponseEntity<?> addPurchase(@Valid @ModelAttribute Purchase purchaseInfo){
		System.out.println(purchaseInfo);
		return ResponseEntity.ok(purchaseInfo);
	}
	
}
