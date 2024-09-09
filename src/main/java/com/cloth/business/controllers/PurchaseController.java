package com.cloth.business.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.business.configurations.annotations.CheckRoles;
import com.cloth.business.entities.Purchase;
import com.cloth.business.services.PurchaseServices;

import jakarta.validation.Valid;


@RestController()
@RequestMapping("/api/v1/purchases")
public class PurchaseController {
	
	@Autowired
	private PurchaseServices purchaseServices;

	@CheckRoles({"ROLE_ADMIN", "ROLE_PURCHASE_CREATE"})
	@PostMapping
	public ResponseEntity<?> addPurchase(@Valid @ModelAttribute Purchase purchaseInfo){
		Purchase purchase = purchaseServices.createPurchase(purchaseInfo);
		return ResponseEntity.ok(purchase);
	}
	
}
